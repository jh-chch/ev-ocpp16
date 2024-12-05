package com.ev.ocpp16.domain.common.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.ev.ocpp16.domain")
public class ApiExceptionAdvice {

	// API 예외 처리
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException e) {
		var status = e.getApiExceptionStatus();
		var errorResponse = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.detail(status.getResultMessage())
				.errorCode(status.getResultCode())
				.build();

		log.error("ApiException: {}", errorResponse);

		return ResponseEntity
				.status(status.getStatus())
				.body(errorResponse);
	}

	// 유효성 검사 예외 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e) {
		var fieldError = e.getBindingResult().getFieldError();
		var errorResponse = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.detail(String.format("'%s' %s (Rejected value: %s)",
						fieldError.getField(),
						fieldError.getDefaultMessage(),
						fieldError.getRejectedValue()))
				.errorCode("Bad Request")
				.build();

		log.error("Validation error: {}", errorResponse);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 인증 실패 예외 처리
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
		var status = ApiExceptionStatus.INVALID_CREDENTIALS;

		var errorResponse = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.detail(status.getResultMessage())
				.errorCode(status.getResultCode())
				.build();

		log.error("AuthenticationException: {}", errorResponse);

		return ResponseEntity.status(status.getStatus()).body(errorResponse);
	}
}
