package com.ev.ocpp16.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargingManagement.dto.ChargerInfoUpdateRequestDTO;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistoryDetail;
import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargerErrorCode;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectionStatus;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectorStatus;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerException;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerNotFoundException;
import com.ev.ocpp16.domain.chargingManagement.service.ChargerCommandService;
import com.ev.ocpp16.domain.chargingManagement.service.ChargerQueryService;
import com.ev.ocpp16.domain.chargingManagement.service.HistoryCommandService;
import com.ev.ocpp16.domain.chargingManagement.service.HistoryQueryService;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.exception.MemberException;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.member.service.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChargingManageService {

    private final MemberQueryService memberQueryService;
    private final ChargerQueryService chargerQueryService;
    private final ChargerCommandService chargerCommandService;
    private final HistoryQueryService historyQueryService;
    private final HistoryCommandService historyCommandService;

    /*
     * 모든 충전기 연결 상태 업데이트
     * 
     * @param connectionStatus 충전기 연결 상태
     */
    public void updateAllChargerConnectionStatus(ConnectionStatus connectionStatus) {
        chargerCommandService.updateAllChargerConnectionStatus(connectionStatus);
    }

    /*
     * 충전기 상태 업데이트
     * 
     * @param chargerIdentifier 충전기 식별자
     * 
     * @param connectionStatus 충전기 연결 상태
     * 
     * @throws ChargerNotFoundException
     */
    public void updateChargerConnectionStatus(String chargerIdentifier, ConnectionStatus connectionStatus)
            throws ChargerNotFoundException {
        Charger findCharger = chargerQueryService.validateChargerForCharging(chargerIdentifier);
        chargerCommandService.updateChargerConnectionStatus(findCharger, connectionStatus);
    }

    /*
     * 충전기 조회
     * 
     * @param chargerIdentifier 충전기 식별자
     * 
     * @throws ChargerNotFoundException
     */
    public void validateChargerForCharging(String chargerIdentifier) throws ChargerNotFoundException {
        chargerQueryService.validateChargerForCharging(chargerIdentifier);
    }

    /**
     * idToken을 검증하여 충전 가능한 회원인지 확인
     * 
     * @param idToken 회원 식별자
     * @throws MemberNotFoundException
     * @throws MemberException
     */
    public void validateMemberForCharging(String idToken) throws MemberNotFoundException, MemberException {
        memberQueryService.validateMemberForCharging(idToken);
    }

    /**
     * 충전기 정보 업데이트
     * 
     * @param chargerIdentifier 충전기 식별자
     * @param dto               충전기 정보 업데이트 요청 DTO
     * @throws ChargerNotFoundException
     */
    public void updateChargerInfo(String chargerIdentifier, ChargerInfoUpdateRequestDTO dto)
            throws ChargerNotFoundException {
        Charger findCharger = chargerQueryService.validateChargerForCharging(chargerIdentifier);
        chargerCommandService.updateChargerInfo(findCharger, dto);
    }

    /**
     * 충전기 커넥터 상태 업데이트 및 오류 로깅
     * 
     * @param chargerIdentifier 충전기 식별자
     * @param connectorId       충전기 커넥터 ID
     * @param connectorStatus   충전기 커넥터 상태
     * @param errorCode         충전기 오류 코드
     * @throws ChargerNotFoundException
     * @throws ChargerConnectorNotFoundException
     */
    public void processChargerStatusNotification(
            String chargerIdentifier,
            Integer connectorId,
            ConnectorStatus connectorStatus,
            ChargerErrorCode errorCode) throws ChargerNotFoundException, ChargerConnectorNotFoundException {

        // 충전기 및 커텍터 조회
        Charger findCharger = chargerQueryService.validateChargerForCharging(chargerIdentifier);
        ChargerConnector findChargerConnector = chargerQueryService.getChargerConnector(findCharger, connectorId);

        // 충전기 커넥터 상태 변경
        chargerCommandService.updateChargerConnectorStatus(findChargerConnector, connectorStatus);

        // 충전기 오류 생성
        historyCommandService.createChargerError(findChargerConnector, errorCode);
    }

    /**
     * 충전 트랜잭션 시작
     * 
     * @param chargerIdentifier 충전기 식별자
     * @param connectorId       충전기 커넥터 ID
     * @param idToken           회원 ID
     * @param startDatetime     충전 시작 시간
     * @param endDatetime       충전 종료 시간
     * @param meterValue        충전 미터 값
     * @return 충전 이력 ID
     * @throws ChargerNotFoundException
     * @throws ChargerConnectorNotFoundException
     * @throws ChargerException
     * @throws MemberNotFoundException
     */
    public Integer processStartTransaction(
            String chargerIdentifier,
            Integer connectorId,
            String idToken,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            BigDecimal meterValue)
            throws ChargerNotFoundException, ChargerConnectorNotFoundException, ChargerException,
            MemberNotFoundException {

        // 충전기 및 충전기 커넥터 조회
        Charger findCharger = chargerQueryService.validateChargerForCharging(chargerIdentifier);
        ChargerConnector findChargerConnector = chargerQueryService.getChargerConnector(findCharger, connectorId);

        // 충전기 상태 확인
        if (!chargerQueryService.canStartCharging(findChargerConnector)) {
            throw new ChargerException("충전 시작이 불가능한 상태입니다.");
        }

        // 회원 확인
        Member findMember = memberQueryService.validateMemberForCharging(idToken);

        // 충전 이력 생성
        ChargeHistory savedChargeHistory = historyCommandService.createChargeHistory(
                findChargerConnector,
                findMember,
                startDatetime,
                endDatetime,
                meterValue,
                ChargeStep.START_TRANSACTION);

        // 충전 이력 상세 생성
        historyCommandService.createChargeHistoryDetail(
                savedChargeHistory,
                meterValue,
                startDatetime,
                ChargeStep.START_TRANSACTION);

        return savedChargeHistory.getId();
    }

    /**
     * 충전 미터 값 업데이트
     * 
     * @param transactionId 충전 트랜잭션 ID
     * @param timestamp     충전 시간
     * @param meterValue    충전 미터 값
     * @param chargeStep    충전 단계
     * @throws ChargeHistoryNotFoundException
     * @throws IllegalArgumentException
     */
    public void processMeterValues(Integer transactionId, LocalDateTime timestamp, BigDecimal meterValue,
            ChargeStep chargeStep) throws ChargeHistoryNotFoundException, IllegalArgumentException {
        // 충전 이력 조회
        ChargeHistory findChargeHistory = historyQueryService.getChargeHistory(transactionId);

        // 마지막 충전 이력 상세 조회
        ChargeHistoryDetail findChargeHistoryDetail = historyQueryService
                .getLastChargeHistoryDetail(transactionId);

        // 충전 이력 업데이트
        historyCommandService.updateChargeHistory(
                findChargeHistory,
                findChargeHistoryDetail.getMeterValue(),
                meterValue,
                timestamp,
                chargeStep);

        // 충전 이력 상세 저장
        historyCommandService.createChargeHistoryDetail(
                findChargeHistory,
                meterValue,
                timestamp,
                chargeStep);
    }
}
