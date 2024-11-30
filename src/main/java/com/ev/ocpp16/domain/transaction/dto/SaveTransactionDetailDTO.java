package com.ev.ocpp16.domain.transaction.dto;

import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.SaveTransactionDTO;

import lombok.Getter;

@Getter
public class SaveTransactionDetailDTO extends SaveTransactionDTO {
    Integer transactionId;

    public SaveTransactionDetailDTO(SaveTransactionDTO dto, Integer transactionId) {
        super(dto.getIdToken(), dto.getChgrId(), dto.getConnectorId(), dto.getTimestamp(), dto.getMeterValue());
        this.transactionId = transactionId;
    }
}
