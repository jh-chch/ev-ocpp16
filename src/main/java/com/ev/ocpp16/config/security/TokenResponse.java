package com.ev.ocpp16.config.security;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private final String token;
    private final Date issuedAt;
    private final Date expiresAt;
} 