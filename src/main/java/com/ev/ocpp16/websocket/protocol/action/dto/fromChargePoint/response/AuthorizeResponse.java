package com.ev.ocpp16.websocket.protocol.action.dto.fromChargePoint.response;

import com.ev.ocpp16.websocket.protocol.action.dto.types.IdTagInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class AuthorizeResponse {
    private final IdTagInfo idTagInfo;
}
