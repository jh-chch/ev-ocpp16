package com.ev.ocpp16.domain.common.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.ev.ocpp16.domain")
public class ApiExceptionAdvice {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException e) {
        var status = e.getApiExceptionStatus();
        var errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                status.getResultMessage(),
                status.getResultCode());

        log.error("ApiException: {}", errorResponse);

        return ResponseEntity
                .status(status.getStatus())
                .body(errorResponse);
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class ApiErrorResponse {
        private final LocalDateTime timestamp;
        private final String detail;
        private final String errorCode;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var fieldError = e.getBindingResult().getFieldError();
        var errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                String.format("'%s' %s (Rejected value: %s)",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()),
                "BAD_REQUEST");

        log.error("Validation error: {}", errorResponse);

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
