package com.ev.ocpp16.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.ev.ocpp16.global.utils.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

	// API 예외 처리
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException e) {
		var status = e.getApiExceptionStatus();
		var errorResponse = ApiExceptionResponse.builder()
				.timestamp(DateTimeUtil.currentKoreanLocalDateTime())
				.detail(status.getMessage(e.getMessage()))
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
				.timestamp(DateTimeUtil.currentKoreanLocalDateTime())
				.detail(String.format("'%s' %s (Rejected value: %s)",
						fieldError.getField(),
						fieldError.getDefaultMessage(),
						fieldError.getRejectedValue()))
				.errorCode(ApiExceptionStatus.INVALID_VALUE.getResultCode())
				.build();

		log.error("Validation error: {}", errorResponse);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// PathVariable, RequestParam 유효성 검사 예외 처리
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ApiExceptionResponse> handleHandlerMethodValidationException(
			HandlerMethodValidationException e) {
		var violation = e.getAllValidationResults().iterator().next();
		var field = violation.getMethodParameter().getParameterName();

		var errorResponse = ApiExceptionResponse.builder()
				.timestamp(DateTimeUtil.currentKoreanLocalDateTime())
				.detail(String.format("'%s' %s (Rejected value: %s)",
						field,
						violation.getResolvableErrors().get(0).getDefaultMessage(),
						violation.getArgument()))
				.errorCode(ApiExceptionStatus.INVALID_VALUE.getResultCode())
				.build();

		log.error("Validation error: {}", errorResponse);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 리소스를 찾을 수 없는 경우 예외 처리
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiExceptionResponse> handleNoResourceFoundException(
			NoResourceFoundException e) {
		var errorResponse = ApiExceptionResponse.builder()
				.timestamp(DateTimeUtil.currentKoreanLocalDateTime())
				.detail(String.format("Resource not found: %s %s",
						e.getHttpMethod(),
						e.getResourcePath()))
				.errorCode("Not Found")
				.build();

		log.error("Resource not found: {}", errorResponse);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	// 인증 실패 예외 처리
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
		var status = ApiExceptionStatus.INVALID_CREDENTIALS;

		var errorResponse = ApiExceptionResponse.builder()
				.timestamp(DateTimeUtil.currentKoreanLocalDateTime())
				.detail(status.getResultMessage())
				.errorCode(status.getResultCode())
				.build();

		log.error("AuthenticationException: {}", errorResponse);

		return ResponseEntity.status(status.getStatus()).body(errorResponse);
	}
}
