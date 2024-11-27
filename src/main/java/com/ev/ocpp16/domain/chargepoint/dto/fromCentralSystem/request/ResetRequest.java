package com.ev.ocpp16.domain.chargepoint.dto.fromCentralSystem.request;

import com.ev.ocpp16.domain.common.dto.ResetType;

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