package com.ev.ocpp16.websocket.dto;

import org.springframework.web.socket.WebSocketSession;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class CallResponse {
    private final WebSocketSession session;
    private final PathInfo pathInfo;
    private final CallRequest<JsonNode> callRequest;
    private final String sendMessage;
}
