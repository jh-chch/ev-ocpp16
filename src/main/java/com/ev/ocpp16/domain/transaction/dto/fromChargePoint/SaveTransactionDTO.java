package com.ev.ocpp16.domain.transaction.dto.fromChargePoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveTransactionDTO {
    String idToken;
    Long chgrId;
    Integer connectorId;
    LocalDateTime timestamp;
    BigDecimal meterValue;
}
