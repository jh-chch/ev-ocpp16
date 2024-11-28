package com.ev.ocpp16.websocket.protocol.message;

import static com.ev.ocpp16.websocket.utils.Constants.CALL_RESULT_SERVER_TO_CLIENT;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.CallResponse;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CallResultProcessor implements MessageProcessor {

    private final ObjectMapper objectMapper;

    @Override
    public CallResponse process(WebSocketSession session, CallRequest<JsonNode> callRequest) throws Exception {
        PathInfo pathInfo = PathInfo.from(session);
        return new CallResponse(session, pathInfo, callRequest, createMessage(pathInfo, callRequest));
    }

    @Override
    public boolean support(JsonNode jsonNode) {
        return jsonNode.get(0).asInt() == Constants.CALL_RESULT_SERVER_TO_CLIENT;
    }

    @Override
    public void validate(JsonNode jsonNode) {
        if (!jsonNode.isArray() || jsonNode.size() != 3) {
            throw new OcppException(
                    jsonNode.get(1).asText(),
                    ErrorCode.PROTOCOL_ERROR,
                    "Invalid Payload JSON array size: expected 3, found " + jsonNode.size());
        }
    }

    @Override
    public CallRequest<JsonNode> parse(JsonNode jsonNode) {
        return new CallRequest<JsonNode>(
                jsonNode.get(0).asInt(),
                jsonNode.get(1).asText(),
                jsonNode.get(2));
    }

    private String createMessage(PathInfo pathInfo, CallRequest<JsonNode> callRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                new Object[] { CALL_RESULT_SERVER_TO_CLIENT, callRequest.getUniqueId(), callRequest.getPayload() });
    }
}
