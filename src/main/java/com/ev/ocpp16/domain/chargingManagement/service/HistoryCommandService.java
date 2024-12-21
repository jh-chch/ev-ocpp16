package com.ev.ocpp16.domain.chargingManagement.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistoryDetail;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargerErrorHistory;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargerErrorCode;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargeHistoryDetailRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargeHistoryRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargerErrorHistoryRepository;
import com.ev.ocpp16.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryCommandService {

    private final ChargeHistoryRepository chargeHistoryRepository;
    private final ChargeHistoryDetailRepository chargeHistoryDetailRepository;
    private final ChargerErrorHistoryRepository chargerErrorHistoryRepository;

    /**
     * 충전기 오류 로깅
     * 
     * @param chargerConnector
     * @param errorCode
     */
    public void createChargerError(ChargerConnector chargerConnector, ChargerErrorCode errorCode) {
        if (chargerConnector == null) {
            return;
        }

        if (ChargerErrorCode.NO_ERROR != errorCode) {
            ChargerErrorHistory chargerErrorHistory = ChargerErrorHistory.builder()
                    .errorCode(errorCode)
                    .info(errorCode.getInfo())
                    .chargerConnector(chargerConnector)
                    .build();
            chargerErrorHistoryRepository.save(chargerErrorHistory);
        }
    }

    /**
     * 충전 이력 생성
     * 
     * @param chargerConnector
     * @param member
     * @param startDatetime
     * @param endDatetime
     * @param meterValue
     * @return
     */
    public ChargeHistory createChargeHistory(
            ChargerConnector chargerConnector,
            Member member,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            BigDecimal meterValue,
            ChargeStep chargeStep) {
        if (chargerConnector == null || member == null) {
            return null;
        }

        ChargeHistory chargeHistory = ChargeHistory.builder()
                .startDatetime(startDatetime)
                .endDatetime(endDatetime)
                .totalMeterValue(meterValue)
                .totalPrice(BigDecimal.ZERO)
                .chargeStep(chargeStep)
                .chargerConnector(chargerConnector)
                .member(member)
                .build();

        return chargeHistoryRepository.save(chargeHistory);
    }

    /**
     * 충전 이력 상세 생성
     * 
     * @param chargeHistory
     * @param meterValue
     * @param timestamp
     */
    public void createChargeHistoryDetail(
            ChargeHistory chargeHistory,
            BigDecimal meterValue,
            LocalDateTime timestamp,
            ChargeStep chargeStep) {
        if (chargeHistory == null) {
            return;
        }

        ChargeHistoryDetail detail = ChargeHistoryDetail.builder()
                .chargeHistory(chargeHistory)
                .chargeStep(chargeStep)
                .actionDatetime(timestamp)
                .meterValue(meterValue)
                .unitPrice(BigDecimal.ZERO)
                .build();

        chargeHistoryDetailRepository.save(detail);
    }

    /**
     * 충전 이력 업데이트
     * 
     * @param chargeHistory  충전 이력
     * @param lastMeterValue 이전 미터 값
     * @param newMeterValue  새로운 미터 값
     * @param timestamp      업데이트 시간
     * @param chargeStep     충전 단계
     */
    public void updateChargeHistory(
            ChargeHistory chargeHistory,
            BigDecimal lastMeterValue,
            BigDecimal newMeterValue,
            LocalDateTime timestamp,
            ChargeStep chargeStep) {
        if (chargeHistory == null) {
            return;
        }

        // 충전 이력 수정
        chargeHistory.changeMeterValueAndChargeStep(newMeterValue, lastMeterValue, timestamp, chargeStep);

        // 충전 이력 저장
        chargeHistoryRepository.save(chargeHistory);
    }
}
