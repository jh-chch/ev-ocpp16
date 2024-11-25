package com.ev.ocpp16.websocket;

import static com.ev.ocpp16.websocket.utils.Constants.SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
class SessionManagerTest {

    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
    }

    private WebSocketSession createMockSession(String sessionKey) {
        WebSocketSession session = mock(WebSocketSession.class);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(SESSION_KEY, sessionKey);
        when(session.getAttributes()).thenReturn(attributes);
        return session;
    }

    private WebSocketSession createMockSessionWithOpen(String sessionKey, boolean isOpen) {
        WebSocketSession session = createMockSession(sessionKey);
        when(session.isOpen()).thenReturn(isOpen);
        return session;
    }

    @Nested
    class AddSession {

        @Test
        @DisplayName("새로운 세션을 성공적으로 추가한다")
        void addsNewSessionSuccessfully() {
            // given
            WebSocketSession session = createMockSession("testCharger");

            // when
            sessionManager.addSession(session);

            // then
            assertThat(sessionManager.getSessionMap()).hasSize(1);
            assertThat(sessionManager.getSessionMap().get("testCharger")).isEqualTo(session);
        }

        @Test
        @DisplayName("중복된 세션 키가 있으면 새 세션을 종료하고 기존 세션을 유지한다")
        void keepsExistingSessionWhenDuplicateKeyProvided() throws IOException {
            // given
            String sessionKey = "testCharger";
            WebSocketSession existingSession = createMockSessionWithOpen(sessionKey, true);
            WebSocketSession newSession = createMockSession(sessionKey);

            // when
            sessionManager.addSession(existingSession);
            sessionManager.addSession(newSession);

            // then
            verify(newSession).close();
            assertThat(sessionManager.getSessionMap()).hasSize(1);
            assertThat(sessionManager.getSessionMap().get(sessionKey)).isEqualTo(existingSession);
        }

    }

    @Test
    @DisplayName("존재하는 세션을 성공적으로 제거한다")
    void removesExistingSessionSuccessfully() {
        // given
        WebSocketSession session = createMockSession("testCharger");
        sessionManager.addSession(session);

        // when
        sessionManager.removeSession(session);

        // then
        assertThat(sessionManager.getSessionMap()).isEmpty();
    }

}
