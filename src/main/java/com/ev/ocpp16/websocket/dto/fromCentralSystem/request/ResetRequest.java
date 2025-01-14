package com.ev.ocpp16.websocket.dto.fromCentralSystem.request;

import com.ev.ocpp16.websocket.dto.fromChargePoint.common.ResetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetRequest {
    private ResetType type; // 1..1
}