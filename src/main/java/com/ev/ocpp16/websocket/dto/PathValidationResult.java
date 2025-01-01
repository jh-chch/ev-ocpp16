package com.ev.ocpp16.websocket.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PathValidationResult {
    private final String userType;
    // private final String version;
    // private final String siteIdentifier;
    private final String chargerIdentifier;
    private final boolean valid;

    public static PathValidationResult invalid() {
        return PathValidationResult.builder()
                .valid(false)
                .build();
    }

    public boolean isValid() {
        return valid;
    }
}