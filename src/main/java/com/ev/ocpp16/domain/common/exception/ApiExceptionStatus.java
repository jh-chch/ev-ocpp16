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
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DUP_EMAIL", "중복된 이메일입니다."),
    DUPLICATE_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "DUP_PHONE_NUMBER", "중복된 전화번호입니다."),
    DUPLICATE_CAR_NUMBER(HttpStatus.BAD_REQUEST, "DUP_CAR_NUMBER", "중복된 차량번호입니다."),

    INVALID_SITE_NAME(HttpStatus.BAD_REQUEST, "INV_SITE_NAME", "존재하지 않는 사이트입니다."),
    INVALID_SERIAL_NUMBER(HttpStatus.BAD_REQUEST, "INV_SERIAL_NUMBER", "존재하지 않는 충전기입니다.");

    private final HttpStatus status;
    private final String resultCode;
    private final String resultMessage;
}
