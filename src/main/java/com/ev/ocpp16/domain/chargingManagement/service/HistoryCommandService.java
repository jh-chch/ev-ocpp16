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
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryException;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargeHistoryDetailRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargeHistoryRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargerErrorHistoryRepository;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.site.entity.SiteRate;

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
     * @param chargerConnector 충전기 커넥터
     * @param member           충전 중인 회원
     * @param startDatetime    충전 시작 시간
     * @return 충전 이력
     */
    public ChargeHistory createChargeHistory(
            ChargerConnector chargerConnector,
            Member member,
            LocalDateTime startDatetime) {
        // 충전 이력 생성
        ChargeHistory chargeHistory = ChargeHistory.builder()
                .chargerConnector(chargerConnector)
                .member(member)
                .startDatetime(startDatetime)
                .build();

        return chargeHistoryRepository.save(chargeHistory);
    }

    /**
     * 충전 이력 상세 생성
     * 
     * @param chargeHistory  충전 이력
     * @param siteRate       사이트 단가
     * @param meterValue     충전량
     * @param actionDatetime 충전량이 들어온 시간
     * @param chargeStep     충전 단계
     * @throws ChargeHistoryException
     */
    public void createChargeHistoryDetail(
            ChargeHistory chargeHistory,
            SiteRate siteRate,
            BigDecimal meterValue,
            LocalDateTime actionDatetime,
            ChargeStep chargeStep) throws ChargeHistoryException {
        ChargeHistoryDetail detail = ChargeHistoryDetail.builder()
                .chargeHistory(chargeHistory)
                .siteRate(siteRate)
                .meterValue(meterValue)
                .actionDatetime(actionDatetime)
                .chargeStep(chargeStep)
                .build();

        chargeHistoryDetailRepository.save(detail);
    }

    /**
     * 충전 이력 업데이트
     * 
     * @param chargeHistory 업데이트할 충전 이력
     * @param newMeterValue 들어온 새로운 미터 값
     * @param timestamp     충전 업데이트 시간
     * @param chargeStep    충전 단계
     * @throws ChargeHistoryException
     */
    public void updateChargeHistory(
            ChargeHistory chargeHistory,
            BigDecimal newMeterValue,
            LocalDateTime timestamp,
            ChargeStep chargeStep) throws ChargeHistoryException {
        // 충전 이력 수정
        chargeHistory.changeHistory(newMeterValue, timestamp, chargeStep);

        // 충전 이력 저장
        chargeHistoryRepository.save(chargeHistory);
    }
}
