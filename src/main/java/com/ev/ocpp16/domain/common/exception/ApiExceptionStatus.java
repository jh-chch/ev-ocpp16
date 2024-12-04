package com.ev.ocpp16.domain.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionStatus {
    INVALID_CREDENTIALS(HttpStatus.FORBIDDEN, "INV_CREDENTIALS", "자격 증명에 실패하였습니다."),
    INVALID_AUTH_HEADER(HttpStatus.FORBIDDEN, "INV_AUTH_HDR", "잘못된 인증 헤더입니다."),
    DUPLICATE_ID_TOKEN(HttpStatus.BAD_REQUEST, "DUP_ID_TOKEN", "중복된 토큰입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DUP_EMAIL", "중복된 이메일입니다.");

    private final HttpStatus status;
    private final String resultCode;
    private final String resultMessage;
}
