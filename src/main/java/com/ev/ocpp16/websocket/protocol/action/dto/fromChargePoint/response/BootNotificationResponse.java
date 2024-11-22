package com.ev.ocpp16.websocket.protocol.action.dto.fromChargePoint.response;

import com.ev.ocpp16.websocket.protocol.action.dto.types.RegistrationStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class BootNotificationResponse {
    private final String currentTime;
    private final Integer interval; // 하트비트 간격(초 단위)
    private final RegistrationStatus status;
}
