package com.ev.ocpp16.domain.chargepoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ChgrInfoUpdateDTO {
    private Long chgrId;
    private String model;
    private String serialNumber;
    private String vendor;
    private String firmwareVersion;
}
