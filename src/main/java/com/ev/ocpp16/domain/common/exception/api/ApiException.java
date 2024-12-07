package com.ev.ocpp16.domain.common.exception.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final ApiExceptionStatus apiExceptionStatus;
}
