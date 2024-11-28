package com.ev.ocpp16.domain.chargepoint.dto;

import com.ev.ocpp16.domain.common.dto.ChargePointErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChgrErrorHstSaveDTO {
    private Long chgrId;
    private Integer connectorId;
    private ChargePointErrorCode errorCode;
    private String errorDescription;
}
