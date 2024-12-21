package com.ev.ocpp16.websocket.dto.fromChargePoint.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReadingContext {
    @JsonProperty("Interruption.Begin")
    INTERRUPTION_BEGIN("Interruption.Begin"), // 중단 시작 시 값 측정

    @JsonProperty("Interruption.End ")
    INTERRUPTION_END("Interruption.End"), // 중단 후 재개 시 값 측정

    @JsonProperty("Other")
    OTHER("Other"), // 다른 상황에 대한 값

    @JsonProperty("Sample.Clock")
    SAMPLE_CLOCK("Sample.Clock"), // 시계에 정렬된 간격으로 측정된 값

    @JsonProperty("Sample.Periodic")
    SAMPLE_PERIODIC("Sample.Periodic"), // 거래 시작 시간을 기준으로 주기적인 샘플로 측정된 값

    @JsonProperty("Transaction.Begin")
    TRANSACTION_BEGIN("Transaction.Begin"), // 거래 종료 시 값 측정

    @JsonProperty("Transaction.End")
    TRANSACTION_END("Transaction.End"), // 거래 시작 시 값 측정

    @JsonProperty("Trigger")
    TRIGGER("Trigger"); // TriggerMessage.req에 대한 응답으로 측정된 값

    private String value;
}
