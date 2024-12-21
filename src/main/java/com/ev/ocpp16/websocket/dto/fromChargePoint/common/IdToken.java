package com.ev.ocpp16.websocket.dto.fromChargePoint.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class IdToken {
    private final String idToken;
}
