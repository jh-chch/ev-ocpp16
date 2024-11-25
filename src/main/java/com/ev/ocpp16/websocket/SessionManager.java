package com.ev.ocpp16.websocket;

import static com.ev.ocpp16.websocket.utils.Constants.SESSION_KEY;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionManager {
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public Map<String, WebSocketSession> getSessionMap() {
        return sessionMap;
    }

    /**
     * 새로운 WebSocket 세션을 추가합니다.
     * 동일한 식별자를 가진 기존 세션이 있다면 새로운 세션을 종료하고 기존 세션을 유지
     */
    public void addSession(WebSocketSession session) {
        validateSession(session);
        String sessionKey = getSessionKey(session);

        // 기존 SESSION KEY가 있고 열려있다면 새로운 SESSION 종료
        if (isSessionValid(sessionKey)) {
            closeSession(session);
            log.warn("기존 세션 유지 - Session ID: {} Session key: {}", sessionMap.get(sessionKey).getId(), sessionKey);
            return;
        }

        sessionMap.put(sessionKey, session);
        log.info(
                "SESSION connection - Session ID: {} Session key: {}, Total active sessions: {}", session.getId(),
                sessionKey, sessionMap.size());
    }

    private void validateSession(WebSocketSession session) {
        if (session == null) {
            throw new IllegalArgumentException("세션은 null일 수 없습니다.");
        }
        if (session.getAttributes().get(SESSION_KEY) == null) {
            throw new IllegalArgumentException("세션 키가 세션 속성에 없습니다.");
        }
    }

    private String getSessionKey(WebSocketSession session) {
        return (String) session.getAttributes().get(SESSION_KEY);
    }

    private boolean isSessionValid(String sessionKey) {
        WebSocketSession session = sessionMap.get(sessionKey);
        return session != null && session.isOpen();
    }

    private void closeSession(WebSocketSession session) {
        try {
            session.close();
            log.info("세션 종료 - Session ID: {} Session key: {}", session.getId(), getSessionKey(session));
        } catch (IOException e) {
            log.error("세션 종료 실패 - Session ID: {} Session key: {}", session.getId(), getSessionKey(session), e);
        }
    }

    public void removeSession(WebSocketSession session) {
        String sessionKey = getSessionKey(session);
        sessionMap.remove(sessionKey);
        log.info("SESSION clear: {} Session key: {}", session, sessionKey);
    }

}
