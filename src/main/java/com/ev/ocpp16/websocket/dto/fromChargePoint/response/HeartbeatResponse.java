package com.ev.ocpp16.websocket.dto.fromChargePoint.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class HeartbeatResponse {
    private final String currentTime;
}
