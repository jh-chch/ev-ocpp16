package com.ev.ocpp16.domain.transaction.exception;

public class ChargerErrorNotFoundException extends Exception {

    public ChargerErrorNotFoundException(String errorCode) {
        super("Charger error not found: " + errorCode);
    }
}
