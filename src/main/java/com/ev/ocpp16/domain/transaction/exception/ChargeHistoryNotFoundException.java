package com.ev.ocpp16.domain.transaction.exception;

public class ChargeHistoryNotFoundException extends Exception {
    public ChargeHistoryNotFoundException(Integer transactionId) {
        super("Charge history not found with transactionId: " + transactionId);
    }
}
