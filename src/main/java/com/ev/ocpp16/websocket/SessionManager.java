package com.ev.ocpp16.websocket;

import static com.ev.ocpp16.websocket.utils.Constants.SESSION_KEY;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.utils.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionManager {
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final ChargerService chargerService;

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
            return;
        }

        // 소켓 연결 시 충전기 상태 CONNECTED 상태로 변경
        chargerService.updateChgrConnSt(PathInfo.from(session).getChgrId(), ChgrConnSt.CONNECTED);

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
        WebSocketSession existingSession = sessionMap.get(sessionKey);

        if (existingSession != null && existingSession.getId().equals(session.getId())) {
            // 소켓 연결 종료 시 충전기 상태 DISCONNECTED 상태로 변경
            chargerService.updateChgrConnSt(PathInfo.from(session).getChgrId(), ChgrConnSt.DISCONNECTED);
            
            sessionMap.remove(sessionKey);
            MDC.remove((String) session.getAttributes().get(Constants.MDC_KEY));
            log.info("SESSION clear: {} Session key: {}", session, sessionKey);
        }
    }

}
