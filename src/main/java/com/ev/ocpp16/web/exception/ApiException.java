package com.ev.ocpp16.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final ApiExceptionStatus apiExceptionStatus;

    public ApiException(ApiExceptionStatus apiExceptionStatus, String customMessage) {
        super(apiExceptionStatus.getMessage(customMessage));
        this.apiExceptionStatus = apiExceptionStatus;
    }
}
