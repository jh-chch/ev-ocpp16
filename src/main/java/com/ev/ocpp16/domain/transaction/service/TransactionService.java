package com.ev.ocpp16.domain.transaction.service;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint.ChgrErrorHstSaveDTO;
import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargepoint.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerConnectorRepository;
import com.ev.ocpp16.domain.common.dto.ChargePointStatus;
import com.ev.ocpp16.domain.common.exception.api.ApiException;
import com.ev.ocpp16.domain.common.exception.api.ApiExceptionStatus;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.member.repository.MemberRepository;
import com.ev.ocpp16.domain.transaction.dto.api.ChgrHstQueryDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.TransactionDetailSaveDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.TransactionSaveDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.TransactionUpdateDTO;
import com.ev.ocpp16.domain.transaction.entity.ChargeHistory;
import com.ev.ocpp16.domain.transaction.entity.ChargeHistoryDetail;
import com.ev.ocpp16.domain.transaction.entity.ChargerError;
import com.ev.ocpp16.domain.transaction.entity.ChargerErrorHistory;
import com.ev.ocpp16.domain.transaction.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.transaction.exception.ChargerErrorNotFoundException;
import com.ev.ocpp16.domain.transaction.repository.ChargeHistoryDetailRepository;
import com.ev.ocpp16.domain.transaction.repository.ChargeHistoryRepository;
import com.ev.ocpp16.domain.transaction.repository.ChargerErrorHistoryRepository;
import com.ev.ocpp16.domain.transaction.repository.ChargerErrorRepository;
import com.ev.ocpp16.domain.transaction.repository.api.TransactionApiRepository;

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

	private final TransactionApiRepository transactionApiRepository;

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
	public Integer saveTransaction(TransactionSaveDTO dto)
			throws MemberNotFoundException, ChargerConnectorNotFoundException {
		// 충전 이력 생성
		ChargeHistory chargeHistory = makeChargeHistory(dto);

		// 충전 이력 저장
		return chargeHistoryRepository.save(chargeHistory).getId();
	}

	// 충전 이력 생성
	private ChargeHistory makeChargeHistory(TransactionSaveDTO dto)
			throws MemberNotFoundException, ChargerConnectorNotFoundException {
		// 회원 조회
		Member findMember = memberRepository.findByIdToken(dto.getIdToken())
				.orElseThrow(() -> new MemberNotFoundException(dto.getIdToken()));

		// 충전기 커넥터 조회
		ChargerConnector findChgrConn = chargerConnectorRepository
				.findByChargerIdAndConnectorId(dto.getChgrId(), dto.getConnectorId())
				.orElseThrow(() -> new ChargerConnectorNotFoundException(dto.getChgrId(), dto.getConnectorId()));

		return new ChargeHistory(
				dto.getTimestamp(), dto.getTimestamp(),
				dto.getMeterValue(), BigDecimal.ZERO,
				dto.getChargeStep(),
				findChgrConn, findMember);
	}

	// 충전 이력 상세 저장
	public void saveTransactionDetail(TransactionDetailSaveDTO dto)
			throws MemberNotFoundException, ChargerConnectorNotFoundException, ChargeHistoryNotFoundException {
		// 충전 이력 조회
		ChargeHistory findChgrHst = chargeHistoryRepository.findById(dto.getTransactionId())
				.orElseThrow(() -> new ChargeHistoryNotFoundException(dto.getTransactionId()));

		// 충전 이력 상세 저장
		ChargeHistoryDetail chargeHistoryDetail = new ChargeHistoryDetail(
				dto.getChargeStep(), dto.getTimestamp(),
				dto.getMeterValue(), BigDecimal.ZERO, findChgrHst);

		chargeHistoryDetailRepository.save(chargeHistoryDetail);
	}

	// 충전 이력 업데이트
	public void updateTransaction(TransactionUpdateDTO dto) throws ChargeHistoryNotFoundException {
		// 충전 이력 조회
		ChargeHistory findChgrHst = chargeHistoryRepository.findById(dto.getTransactionId())
				.orElseThrow(() -> new ChargeHistoryNotFoundException(dto.getTransactionId()));

		// 가장 최근 충전량 조회
		BigDecimal findMeterValue = chargeHistoryDetailRepository
				.findFirstByChargeHistoryIdOrderByIdDesc(dto.getTransactionId())
				.map(ChargeHistoryDetail::getMeterValue)
				.orElse(BigDecimal.ZERO);

		// 총 충전량 업데이트
		findChgrHst.calculateTotalMeterValue(dto.getMeterValue(), findMeterValue);

		// 충전 이력 업데이트
		findChgrHst.changeChgrHst(dto.getTimestamp(), dto.getChargeStep());
	}

	// 충전 이력 조회
	public List<ChgrHstQueryDTO.Response> getTransactions(ChgrHstQueryDTO.Request dto) {
		return transactionApiRepository.findChgrHsts(dto)
				.orElseThrow(() -> new ApiException(ApiExceptionStatus.NOT_FOUND_CHARGE_HISTORY));
	}
}
