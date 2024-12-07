package com.ev.ocpp16.domain.common.exception.api;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ApiExceptionResponse {
    private final LocalDateTime timestamp;
    private final String detail;
    private final String errorCode;
}
