package com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ChgrInfoUpdateDTO {
    private Long chgrId;
    private String model;
    private String serialNumber;
    private String vendor;
    private String firmwareVersion;
}
