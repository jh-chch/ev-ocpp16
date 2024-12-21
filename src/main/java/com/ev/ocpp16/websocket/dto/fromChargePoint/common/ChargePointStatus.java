package com.ev.ocpp16.websocket.dto.fromChargePoint.common;

import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectorStatus;

public enum ChargePointStatus {
    Available,
    Preparing,
    Charging,
    SuspendedEVSE,
    SuspendedEV,
    Finishing,
    Reserved,
    Unavailable,
    Faulted;

    public ConnectorStatus toConnectorStatus() {
        switch (this) {
            case Available:
                return ConnectorStatus.AVAILABLE;
            case Preparing:
                return ConnectorStatus.PREPARING;
            case Charging:
                return ConnectorStatus.CHARGING;
            case SuspendedEVSE:
                return ConnectorStatus.SUSPENDED_EVSE;
            case SuspendedEV:
                return ConnectorStatus.SUSPENDED_EV;
            case Finishing:
                return ConnectorStatus.FINISHING;
            case Reserved:
                return ConnectorStatus.RESERVED;
            case Unavailable:
                return ConnectorStatus.UNAVAILABLE;
            case Faulted:
                return ConnectorStatus.FAULTED;
            default:
                throw new IllegalArgumentException("Unknown ChargePointStatus: " + this);
        }
    }
}
