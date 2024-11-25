package com.ev.ocpp16.websocket.exception;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OcppExceptionHandler {

    /**
     * OcppException을 처리하고 WebSocket 세션에 오류 메시지를 전송
     *
     * @param session
     * @param e
     */
    public void handleOcppException(WebSocketSession session, OcppException e) {
        try {
            String errorMessage = e.createErrorMessage();
            session.sendMessage(new TextMessage(errorMessage));
            log.info("<==== [handleOcppException] {}", errorMessage);
        } catch (IOException ioException) {
            log.error("[handleOcppException] 세션 오류 메시지 전송 실패: {}", session.getId(),
                    ioException);
        }
    }
}
