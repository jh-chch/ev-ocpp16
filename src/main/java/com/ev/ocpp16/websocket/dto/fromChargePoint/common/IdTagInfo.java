package com.ev.ocpp16.websocket.dto.fromChargePoint.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class IdTagInfo {
    private String expiryDate;
    private String parentIdTag;
    private final AuthorizationStatus status;
}