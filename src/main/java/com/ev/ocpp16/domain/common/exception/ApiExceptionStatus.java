package com.ev.ocpp16.domain.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionStatus {
    DUPLICATE_ID_TOKEN(HttpStatus.BAD_REQUEST, "DUP_ID_TOKEN", "중복된 토큰입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DUP_EMAIL", "중복된 이메일입니다.");

    private final HttpStatus status;
    private final String resultCode;
    private final String resultMessage;
}
