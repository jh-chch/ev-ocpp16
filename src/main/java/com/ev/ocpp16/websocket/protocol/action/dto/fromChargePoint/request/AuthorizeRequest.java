package com.ev.ocpp16.websocket.protocol.action.dto.fromChargePoint.request;

import com.ev.ocpp16.websocket.protocol.action.dto.types.IdToken;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthorizeRequest {
    private IdToken idTag;
}