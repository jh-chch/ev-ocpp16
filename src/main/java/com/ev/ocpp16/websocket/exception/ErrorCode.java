package com.ev.ocpp16.websocket.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter @ToString
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_IMPLEMENTED("NotImplemented", "Requested Action is not known by receiver"), // 없는 액션
    NOT_SUPPORTED("NotSupported", "Requested Action is recognized but not supported by the receiver"), // 지원하지 않는 액션
    INTERNAL_ERROR("InternalError", "An internal error occurred and the receiver was not able to process the requested Action successfully"), // 서버 내부 오류
    PROTOCOL_ERROR("ProtocolError", "Payload for Action is incomplete"), // 페이로드가 불완전함
    SECURITY_ERROR("SecurityError", "During the processing of Action a security issue occurred preventing receiver from completing the Action successfully"), // 액션 처리 중 보안 문제
    FORMATION_VIOLATION("FormationViolation", "Payload for Action is syntactically incorrect or not conform the PDU structure for Action"), // 액션의 페이로드가 문법적으로 잘못되었거나 PDU 구조를 준수하지 않음
    PROPERTY_CONSTRAINT_VIOLATION("PropertyConstraintViolation", "Payload is syntactically correct but at least one field contains an invalid value"), // 액션의 페이로드가 문법적으로 올바르지만 하나의 필드에 유효하지 않은 값이 포함됨
    OCCURENCE_CONSTRAINT_VIOLATION("OccurenceConstraintViolation", "Payload for Action is syntactically correct but at least one of the fields violates occurence constraints"), // 액션의 페이로드가 문법적으로 올바르지만 적어도 하나의 필드가 발생 제약 조건을 위반함
    TYPE_CONSTRAINT_VIOLATION("TypeConstraintViolation", "Payload for Action is syntactically correct but at least one of the fields violates data type constraints (e.g. “somestring”: 12)"), // 액션의 페이로드가 문법적으로 올바르지만 적어도 하나의 필드가 유효성 검사를 위반함
    GENERIC_ERROR("GenericError", "Any other error not covered by the previous ones"); // 기타 오류

	private final String code;
	private final String description;
}