package com.ev.ocpp16.websocket.protocol.message;

import org.springframework.web.socket.WebSocketSession;

import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.CallResponse;
import com.fasterxml.jackson.databind.JsonNode;

public interface MessageProcessor {
    CallResponse process(WebSocketSession session, CallRequest<JsonNode> callRequest) throws Exception;

    boolean support(JsonNode jsonNode);

    void validate(JsonNode jsonNode);

    CallRequest<JsonNode> parse(JsonNode jsonNode);

}
