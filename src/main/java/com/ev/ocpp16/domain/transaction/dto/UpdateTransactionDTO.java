package com.ev.ocpp16.domain.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ev.ocpp16.domain.transaction.entity.enums.ChargeStep;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTransactionDTO {
    Integer transactionId;
    LocalDateTime timestamp;
    BigDecimal meterValue;
    ChargeStep chargeStep;
}
