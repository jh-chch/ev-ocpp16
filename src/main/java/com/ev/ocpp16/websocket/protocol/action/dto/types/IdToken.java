package com.ev.ocpp16.websocket.protocol.action.dto.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class IdToken {
    private final String idToken;
}
