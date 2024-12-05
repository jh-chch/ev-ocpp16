package com.ev.ocpp16.websocket.dto.fromChargePoint.request;

import lombok.Getter;
import lombok.ToString;

/**
 * @param connectorId   충전 포인트의 커넥터 번호
 * @param idTag         트랜잭션을 시작하기 위한 식별자
 * @param meterStart    트랜잭션 시작 시의 미터 값(Wh)
 * @param reservationId 트랜잭션으로 종료되는 예약의 ID
 * @param timestamp     트랜잭션 시작 날짜
 */
@Getter
@ToString
public class StartTransactionRequest {
    private Integer connectorId; // 1..1
    private String idTag; // 1..1
    private Integer meterStart; // 1..1
    private Integer reservationId;
    private String timestamp; // 1..1
}