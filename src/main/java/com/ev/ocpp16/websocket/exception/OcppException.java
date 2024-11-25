package com.ev.ocpp16.websocket.exception;

import static com.ev.ocpp16.websocket.utils.Constants.CALL_ERROR_SERVER_TO_CLIENT;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OcppException extends RuntimeException {
    private Integer messageType = CALL_ERROR_SERVER_TO_CLIENT;
    private final String uniqueId;
    private final ErrorCode errorCode;
    private final String errorDetails;

    public String createErrorMessage() {
        String jsonFormat = String.format(
                "[%d, \"%s\", \"%s\", \"%s\", {\"errorDetails\": \"%s\"}]",
                messageType,
                uniqueId,
                errorCode.getCode(),
                errorCode.getDescription(),
                errorDetails);
        return jsonFormat;
    }
}