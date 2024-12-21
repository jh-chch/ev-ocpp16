package com.ev.ocpp16.domain.chargingManagement.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargerInfoUpdateRequestDTO {
    private String chargePointModel;
    private String chargePointVendor;
    private String chargePointSerialNumber;
    private String firmwareVersion;
}
