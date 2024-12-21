package com.ev.ocpp16.global.config.security;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String token;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
}