package com.ev.ocpp16.domain.chargepoint.dto;

import com.ev.ocpp16.domain.common.dto.ChargePointStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ChgrConnUpdateDTO {
    private Long chgrId;
    private Integer connectorId;
    private ChargePointStatus chargePointStatus;
}
