package com.ev.ocpp16.config.security;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.ev.ocpp16.websocket.utils.DateTimeUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public TokenResponse generateToken(String email) {
        LocalDateTime issuedAt = DateTimeUtil.currentKoreanLocalDateTime();
        LocalDateTime expiration = issuedAt.plusMinutes(1);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(DateTimeUtil.koreanLocalDateTimeToDate(issuedAt))
                .setExpiration(DateTimeUtil.koreanLocalDateTimeToDate(expiration))
                .signWith(secretKey)
                .compact();

        return TokenResponse.builder()
                .token(token)
                .issuedAt(issuedAt)
                .expiresAt(expiration)
                .build();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, String email) {
        return (email.equals(extractEmail(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        LocalDateTime now = DateTimeUtil.currentKoreanLocalDateTime();
        return expiration.before(DateTimeUtil.koreanLocalDateTimeToDate(now));
    }

}