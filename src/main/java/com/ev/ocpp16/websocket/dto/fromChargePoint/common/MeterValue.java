package com.ev.ocpp16.websocket.dto.fromChargePoint.common;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MeterValue {
    private String timestamp; // 1..1
    private List<SampledValue> sampledValue; // 1..*
}
