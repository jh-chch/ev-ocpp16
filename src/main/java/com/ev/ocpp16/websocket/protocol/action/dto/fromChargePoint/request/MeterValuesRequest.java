package com.ev.ocpp16.websocket.protocol.action.dto.fromChargePoint.request;

import java.util.List;

import com.ev.ocpp16.websocket.protocol.action.dto.types.MeterValue;

import lombok.Getter;
import lombok.ToString;

/**
 * @param connectorId   충전 포인트의 커넥터 번호
 * @param transactionId 거래 ID
 * @param meterValue    샘플링된 미터 값, 타임스탬프
 */
@Getter
@ToString
public class MeterValuesRequest {
    private Integer connectorId; // 1..1
    private Integer transactionId;
    private List<MeterValue> meterValue; // 1..*
}
