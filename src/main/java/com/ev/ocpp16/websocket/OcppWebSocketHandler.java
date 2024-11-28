package com.ev.ocpp16.websocket;

import static com.ev.ocpp16.websocket.utils.Constants.MDC_KEY;

import java.util.List;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.CallResponse;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.exception.OcppExceptionHandler;
import com.ev.ocpp16.websocket.protocol.message.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OcppWebSocketHandler extends TextWebSocketHandler {
    private final List<MessageProcessor> messageProcessors;
    private final OcppExceptionHandler errorHandler;
    private final MessageSender messageSender;
    private final SessionManager sessionManager;
    private final ChargerService chargerService;
    private final ObjectMapper objectMapper;

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
        chargerService.updateChgrConnSt(PathInfo.from(session).getChgrId(), ChgrConnSt.CONNECTED);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MDC.put(MDC_KEY, (String) session.getAttributes().get(MDC_KEY));
        log.info("====> [handleTextMessage] session: {} message: {}", session, message.getPayload());

        try {
            JsonNode jsonNode = parseJsonNode(message);

            // 유효한 메시지 프로세서 조회
            var messageProcessor = messageProcessors.stream()
                    .filter(processor -> processor.support(jsonNode))
                    .findFirst()
                    .orElseThrow(() -> new OcppException(
                            jsonNode.get(1).asText(),
                            ErrorCode.TYPE_CONSTRAINT_VIOLATION,
                            "Invalid message type identifier: " + jsonNode));

            // 메시지 유효성 검사
            messageProcessor.validate(jsonNode);

            // 메시지 파싱
            CallRequest<JsonNode> callRequest = messageProcessor.parse(jsonNode);

            // 메시지 처리
            CallResponse callResponse = messageProcessor.process(session, callRequest);

            // 메시지 전송
            messageSender.sendMessage(callResponse);
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
        chargerService.updateChgrConnSt(PathInfo.from(session).getChgrId(), ChgrConnSt.DISCONNECTED);

        sessionManager.removeSession(session);
        MDC.remove((String) session.getAttributes().get(MDC_KEY));
    }

    private JsonNode parseJsonNode(TextMessage message) {
        try {
            return objectMapper.readTree(message.getPayload());
        } catch (JsonProcessingException e) {
            throw new OcppException("", ErrorCode.FORMATION_VIOLATION, "Failed to parse JSON: " + e.getMessage());
        }
    }
}
