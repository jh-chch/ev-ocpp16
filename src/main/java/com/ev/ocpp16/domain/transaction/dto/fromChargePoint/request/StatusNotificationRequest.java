package com.ev.ocpp16.domain.transaction.dto.fromChargePoint.request;

import com.ev.ocpp16.domain.common.dto.ChargePointErrorCode;
import com.ev.ocpp16.domain.common.dto.ChargePointStatus;

import lombok.Getter;
import lombok.ToString;

/**
 * @param connectorId     충전 포인트의 커넥터 번호
 * @param errorCode       오류 코드
 * @param info            오류 정보
 * @param status          충전소 현재 상태
 * @param timestamp       상태 보고 시간
 * @param vendorId        공급업체 식별자
 * @param vendorErrorCode 공급업체 오류 코드
 */
@Getter
@ToString
public class StatusNotificationRequest {
    private Integer connectorId; // 1..1
    private ChargePointErrorCode errorCode; // 1..1
    private String info;
    private ChargePointStatus status; // 1..1
    private String timestamp;
    private String vendorId;
    private String vendorErrorCode;
}
