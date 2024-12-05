package com.ev.ocpp16.websocket.dto.fromChargePoint.response;

import com.ev.ocpp16.domain.common.dto.IdTagInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class AuthorizeResponse {
    private final IdTagInfo idTagInfo;
}
