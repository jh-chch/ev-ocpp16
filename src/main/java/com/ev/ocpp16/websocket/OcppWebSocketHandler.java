package com.ev.ocpp16.websocket;

import static com.ev.ocpp16.websocket.utils.Constants.MDC_KEY;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.entity.enums.ChgrConnSt;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.exception.OcppExceptionHandler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OcppWebSocketHandler extends TextWebSocketHandler {

    private final OcppExceptionHandler errorHandler;
    private final SessionManager sessionManager;
    private final ChargerService chargerService;

    @PostConstruct
    public void initializeChargerConnectionStatus() {
        // 모든 충전기 상태 DISCONNECTED 상태로 변경
        chargerService.updateAllChgrConnSt(ChgrConnSt.DISCONNECTED);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished");
        sessionManager.addSession(session);

        // 소켓 연결 시 충전기 상태 CONNECTED 상태로 변경
        chargerService.updateChgrConnSt(PathInfo.getPathInfoFromSession(session).getChgrId(), ChgrConnSt.CONNECTED);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            MDC.put(MDC_KEY, (String) session.getAttributes().get(MDC_KEY));
            log.info("====> [handleTextMessage] session: {} message: {}", session, message.getPayload());

        } catch (OcppException e) {
            errorHandler.handleOcppException(session, e);
        } catch (Exception e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage(""));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("afterConnectionClosed");

        // 소켓 연결 종료 시 충전기 상태 DISCONNECTED 상태로 변경
        chargerService.updateChgrConnSt(PathInfo.getPathInfoFromSession(session).getChgrId(), ChgrConnSt.DISCONNECTED);

        sessionManager.removeSession(session);
        MDC.remove((String) session.getAttributes().get(MDC_KEY));
    }

}
