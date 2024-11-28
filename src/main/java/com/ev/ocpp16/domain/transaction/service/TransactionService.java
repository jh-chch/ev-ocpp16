package com.ev.ocpp16.domain.transaction.service;

import static com.ev.ocpp16.domain.common.dto.ChargePointErrorCode.NoError;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.ChgrErrorHstSaveDTO;
import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerConnectorRepository;
import com.ev.ocpp16.domain.transaction.entity.ChargerErrorHistory;
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
    public void saveChgrErrorHst(ChgrErrorHstSaveDTO dto) {
        chargerConnectorRepository
                .findByChargerIdAndConnectorId(dto.getChgrId(), dto.getConnectorId())
                .ifPresent(connector -> saveErrorHistory(connector, dto));
    }

    private void saveErrorHistory(ChargerConnector connector, ChgrErrorHstSaveDTO dto) {
        // NoError가 아닌 경우에만 에러 이력 저장
        if (NoError.equals(dto.getErrorCode())) {
            return;
        }

        // 2. 에러 코드로 ChargerError 조회 후 이력 저장
        chargerErrorRepository
                .findByErrorCode(dto.getErrorCode().name())
                .ifPresent(error -> {
                    ChargerErrorHistory errorHistory = new ChargerErrorHistory(
                            dto.getErrorDescription(),
                            connector,
                            error
                    );
                    chargerErrorHistoryRepository.save(errorHistory);
                });
    }
}
