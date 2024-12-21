package com.ev.ocpp16.websocket.dto.fromChargePoint.request;

import java.util.List;

import com.ev.ocpp16.websocket.dto.fromChargePoint.common.MeterValue;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.Reason;

import lombok.Getter;
import lombok.ToString;

/**
 * @param idTag           트랜잭션을 종료하기 위한 식별자
 * @param meterStop       종료 시 커넥터의 미터 값(Wh)
 * @param timestamp       중단된 날짜
 * @param transactionId   충전 시작 응답에서 받은 거래 ID
 * @param reason          중단된 이유
 * @param transactionData 사용 세부 정보
 */
@Getter
@ToString
public class StopTransactionRequest {
    private String idTag;
    private Integer meterStop; // 1..1
    private String timestamp; // 1..1
    private Integer transactionId; // 1..1
    private Reason reason;
    private List<MeterValue> transactionData; // 0.*
}