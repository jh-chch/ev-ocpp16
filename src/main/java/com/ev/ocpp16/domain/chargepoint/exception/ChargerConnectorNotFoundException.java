package com.ev.ocpp16.domain.chargepoint.exception;

public class ChargerConnectorNotFoundException extends Exception {

    public ChargerConnectorNotFoundException(Long chgrId, Integer connectorId) {
        super("ChargerConnector not found: " + chgrId + ", " + connectorId);
    }
}
