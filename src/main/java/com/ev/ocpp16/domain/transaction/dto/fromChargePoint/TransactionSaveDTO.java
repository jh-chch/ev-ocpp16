package com.ev.ocpp16.domain.transaction.dto.fromChargePoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ev.ocpp16.domain.transaction.entity.enums.ChargeStep;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionSaveDTO {
    String idToken;
    Long chgrId;
    Integer connectorId;
    LocalDateTime timestamp;
    BigDecimal meterValue;
    ChargeStep chargeStep;
}
