package com.ev.ocpp16.web.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionStatus {
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "INV_VALUE", "잘못된 요청입니다."),
    DUPLICATE_VALUE(HttpStatus.BAD_REQUEST, "DUP_VALUE", "중복된 값입니다."),

    INVALID_CREDENTIALS(HttpStatus.FORBIDDEN, "INV_CREDENTIALS", "자격 증명에 실패하였습니다."),
    INVALID_AUTH_HEADER(HttpStatus.FORBIDDEN, "INV_AUTH_HDR", "잘못된 인증 헤더입니다."),

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NOT_FOUND_MEMBER", "회원을 찾을 수 없습니다."),
    NOT_FOUND_SITE(HttpStatus.NOT_FOUND, "NOT_FOUND_SITE", "사이트를 찾을 수 없습니다."),
    NOT_FOUND_CHARGER(HttpStatus.NOT_FOUND, "NOT_FOUND_CHARGER", "충전기를 찾을 수 없습니다."),
    NOT_FOUND_CHARGE_HISTORY(HttpStatus.NOT_FOUND, "NOT_FOUND_CHARGE_HISTORY", "충전 이력을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String resultCode;
    private final String resultMessage;

    public String getMessage(String customMessage) {
        return customMessage != null ? customMessage : this.resultMessage;
    }
}
