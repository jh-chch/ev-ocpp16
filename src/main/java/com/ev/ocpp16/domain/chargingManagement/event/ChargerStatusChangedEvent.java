package com.ev.ocpp16.domain.chargingManagement.event;

import lombok.Getter;

@Getter
public class ChargerStatusChangedEvent {
    private final String siteName;

    public ChargerStatusChangedEvent(String siteName) {
        this.siteName = siteName;
    }
}