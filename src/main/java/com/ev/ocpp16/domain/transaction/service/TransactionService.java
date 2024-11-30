package com.ev.ocpp16.domain.transaction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.ChgrErrorHstSaveDTO;
import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargepoint.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerConnectorRepository;
import com.ev.ocpp16.domain.transaction.entity.ChargerError;
import com.ev.ocpp16.domain.transaction.entity.ChargerErrorHistory;
import com.ev.ocpp16.domain.transaction.exception.ChargerErrorNotFoundException;
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

}
