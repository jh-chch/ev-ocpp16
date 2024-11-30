package com.ev.ocpp16.domain.transaction.service;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.ChgrErrorHstSaveDTO;
import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargepoint.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerConnectorRepository;
import com.ev.ocpp16.domain.common.dto.ChargePointStatus;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.member.repository.MemberRepository;
import com.ev.ocpp16.domain.transaction.dto.SaveTransactionDetailDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.SaveTransactionDTO;
import com.ev.ocpp16.domain.transaction.entity.ChargeHistory;
import com.ev.ocpp16.domain.transaction.entity.ChargeHistoryDetail;
import com.ev.ocpp16.domain.transaction.entity.ChargerError;
import com.ev.ocpp16.domain.transaction.entity.ChargerErrorHistory;
import com.ev.ocpp16.domain.transaction.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.transaction.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.transaction.exception.ChargerErrorNotFoundException;
import com.ev.ocpp16.domain.transaction.repository.ChargeHistoryDetailRepository;
import com.ev.ocpp16.domain.transaction.repository.ChargeHistoryRepository;
import com.ev.ocpp16.domain.transaction.repository.ChargerErrorHistoryRepository;
import com.ev.ocpp16.domain.transaction.repository.ChargerErrorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {
	private final ChargerConnectorRepository chargerConnectorRepository;
	private final ChargerErrorRepository chargerErrorRepository;
	private final ChargerErrorHistoryRepository chargerErrorHistoryRepository;
	private final MemberRepository memberRepository;
	private final ChargeHistoryRepository chargeHistoryRepository;
	private final ChargeHistoryDetailRepository chargeHistoryDetailRepository;

	// NoError 아니면 charger_error_history 저장
	public void saveChgrErrorHst(ChgrErrorHstSaveDTO dto)
			throws ChargerConnectorNotFoundException, ChargerErrorNotFoundException {
		// 충전기 커넥터 조회
		ChargerConnector findChgrConn = chargerConnectorRepository
				.findByChargerIdAndConnectorId(dto.getChgrId(), dto.getConnectorId())
				.orElseThrow(() -> new ChargerConnectorNotFoundException(dto.getChgrId(), dto.getConnectorId()));

		// 에러 코드에 해당하는 ChargerError 조회
		ChargerError findChgrError = chargerErrorRepository
				.findByErrorCode(dto.getErrorCode().name())
				.orElseThrow(() -> new ChargerErrorNotFoundException(dto.getErrorCode().name()));

		// 에러 이력 저장
		ChargerErrorHistory errorHistory = new ChargerErrorHistory(
				dto.getErrorDescription(),
				findChgrConn,
				findChgrError);

		chargerErrorHistoryRepository.save(errorHistory);
	}

	// 충전 시작을 위한 충전기 상태 검사
	public boolean isChgrStValidForStartCharging(Long chgrId, Integer connectorId)
			throws ChargerConnectorNotFoundException {
		ChargerConnector findChgrConn = chargerConnectorRepository
				.findByChargerIdAndConnectorId(chgrId, connectorId)
				.orElseThrow(() -> new ChargerConnectorNotFoundException(chgrId, connectorId));

		ChargePointStatus findChgrSt = findChgrConn.getChargePointStatus();

		// 상태가 Available or Reserved or Preparing 외 충전 시작 불가
		Set<ChargePointStatus> validStatuses = EnumSet.of(
				ChargePointStatus.Available,
				ChargePointStatus.Preparing,
				ChargePointStatus.Reserved);

		return validStatuses.contains(findChgrSt);
	}

	// 충전 이력 저장
	public Integer saveTransaction(SaveTransactionDTO dto)
			throws MemberNotFoundException, ChargerConnectorNotFoundException {
		ChargeHistory chargeHistory = makeChargeHistory(dto);
		ChargeHistory saveChargeHistory = chargeHistoryRepository.save(chargeHistory);
		return saveChargeHistory.getId();
	}

	// 충전 이력 상세 저장
	public void saveTransactionDetail(SaveTransactionDetailDTO dto)
			throws MemberNotFoundException, ChargerConnectorNotFoundException, ChargeHistoryNotFoundException {
		ChargeHistory chargeHistory = chargeHistoryRepository.findById(dto.getTransactionId())
				.orElseThrow(() -> new ChargeHistoryNotFoundException(dto.getTransactionId()));

		ChargeHistoryDetail chargeHistoryDetail = new ChargeHistoryDetail(ChargeStep.START_TRANSACTION,
				dto.getTimestamp(), dto.getMeterValue(), BigDecimal.ZERO, chargeHistory);
		chargeHistoryDetailRepository.save(chargeHistoryDetail);
	}

	private ChargeHistory makeChargeHistory(SaveTransactionDTO dto)
			throws MemberNotFoundException, ChargerConnectorNotFoundException {
		Member findMember = memberRepository.findByIdToken(dto.getIdToken())
				.orElseThrow(() -> new MemberNotFoundException(dto.getIdToken()));

		ChargerConnector findChgrConn = chargerConnectorRepository
				.findByChargerIdAndConnectorId(dto.getChgrId(), dto.getConnectorId())
				.orElseThrow(() -> new ChargerConnectorNotFoundException(dto.getChgrId(), dto.getConnectorId()));

		return new ChargeHistory(
				dto.getTimestamp(),
				dto.getTimestamp(),
				dto.getMeterValue(),
				BigDecimal.ZERO,
				ChargeStep.START_TRANSACTION,
				findChgrConn,
				findMember);
	}

}
