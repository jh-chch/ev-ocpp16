package com.ev.ocpp16.domain.chargepoint.exception;

public class ChargerNotFoundException extends Exception {
    public ChargerNotFoundException(Long chgrId) {
        super("Charger not found: " + chgrId);
    }
}
