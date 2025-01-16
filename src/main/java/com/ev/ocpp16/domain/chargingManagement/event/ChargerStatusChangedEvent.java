package com.ev.ocpp16.domain.chargingManagement.event;

import java.time.LocalDateTime;

import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectionStatus;

import lombok.Getter;

@Getter
public class ChargerStatusChangedEvent {
    private final String siteName;
    private final String chargerIdentifier;
    private final ConnectionStatus connectionStatus;
    private final LocalDateTime timestamp;

    public ChargerStatusChangedEvent(Charger charger, ConnectionStatus connectionStatus) {
        this.siteName = charger.getSite().getName();
        this.chargerIdentifier = charger.getSerialNumber();
        this.connectionStatus = connectionStatus;
        this.timestamp = LocalDateTime.now();
    }
}