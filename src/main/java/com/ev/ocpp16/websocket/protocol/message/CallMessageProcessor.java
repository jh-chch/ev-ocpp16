package com.ev.ocpp16.websocket.protocol.message;

import static com.ev.ocpp16.websocket.utils.Constants.CALL_CLIENT_TO_SERVER;
import static com.ev.ocpp16.websocket.utils.Constants.CALL_RESULT_SERVER_TO_CLIENT;
import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_ADMIN;
import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.CallResponse;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;
import com.ev.ocpp16.websocket.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CallMessageProcessor<T, R> implements MessageProcessor {
    private final Map<String, ActionHandler<T, R>> actionMap;
    private final ObjectMapper objectMapper;

    @Override
    public CallResponse process(WebSocketSession session, CallRequest<JsonNode> callRequest) throws Exception {
        PathInfo pathInfo = PathInfo.from(session);

        ActionHandler<T, R> actionHandler = findActionHandler(pathInfo, callRequest);

        T parsedPayload = validateAndParsePayload(actionHandler, callRequest);

        CallRequest<T> parsedCallRequest = buildCallRequest(callRequest, parsedPayload);

        R handleAction = actionHandler.handleAction(pathInfo, parsedCallRequest);

        return new CallResponse(session, pathInfo, callRequest, createMessage(pathInfo, callRequest, handleAction));
    }

    @Override
    public boolean support(JsonNode jsonNode) {
        return jsonNode.get(0).asInt() == Constants.CALL_CLIENT_TO_SERVER;
    }

    @Override
    public void validate(JsonNode jsonNode) {
        if (!jsonNode.isArray() || jsonNode.size() != 4) {
            throw new OcppException(
                    jsonNode.get(1).asText(),
                    ErrorCode.PROTOCOL_ERROR,
                    "Invalid Payload JSON array size: expected 4, found " + jsonNode.size());
        }
    }

    @Override
    public CallRequest<JsonNode> parse(JsonNode jsonNode) {
        return new CallRequest<JsonNode>(
                jsonNode.get(0).asInt(),
                jsonNode.get(1).asText(),
                jsonNode.get(2).asText(),
                jsonNode.get(3));
    }

    private ActionHandler<T, R> findActionHandler(PathInfo pathInfo, CallRequest<JsonNode> callRequest) {
        String actionKey = pathInfo.getUserType() + callRequest.getAction();
        ActionHandler<T, R> actionHandler = actionMap.get(actionKey);

        if (actionHandler == null) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.NOT_IMPLEMENTED,
                    "Action '" + callRequest.getAction() + "' is not implemented");
        }

        return actionHandler;
    }

    private T validateAndParsePayload(ActionHandler<T, R> actionHandler, CallRequest<JsonNode> callJsonNode) {
        try {
            T payload = objectMapper.treeToValue(callJsonNode.getPayload(), actionHandler.getPayloadClass());

            if (!actionHandler.validateRequiredFields(payload)) {
                throw new OcppException(callJsonNode.getUniqueId(), ErrorCode.PROPERTY_CONSTRAINT_VIOLATION,
                        "Required fields validation failed");
            }

            return payload;
        } catch (OcppException e) {
            throw e;
        } catch (JsonProcessingException e) {
            throw new OcppException(callJsonNode.getUniqueId(), ErrorCode.FORMATION_VIOLATION,
                    "JSON processing error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new OcppException(callJsonNode.getUniqueId(), ErrorCode.FORMATION_VIOLATION,
                    "Invalid argument: " + e.getMessage());
        } catch (Exception e) {
            throw new OcppException(callJsonNode.getUniqueId(), ErrorCode.INTERNAL_ERROR,
                    "Unexpected error: " + e.getMessage());
        }
    }

    private CallRequest<T> buildCallRequest(CallRequest<JsonNode> callRequest, T parsedPayload) {
        return CallRequest.<T>builder()
                .messageTypeId(callRequest.getMessageTypeId())
                .uniqueId(callRequest.getUniqueId())
                .action(callRequest.getAction())
                .payload(parsedPayload)
                .build();
    }

    private String createMessage(PathInfo pathInfo, CallRequest<JsonNode> callRequest, R response)
            throws JsonProcessingException {
        String sendMessage = "";
        final String userType = pathInfo.getUserType();
        if (USER_TYPE_USER.equals(userType)) {
            sendMessage = objectMapper.writeValueAsString(
                    new Object[] { CALL_RESULT_SERVER_TO_CLIENT, callRequest.getUniqueId(), response });
        }
        if (USER_TYPE_ADMIN.equals(userType)) {
            sendMessage = objectMapper.writeValueAsString(
                    new Object[] { CALL_CLIENT_TO_SERVER, callRequest.getUniqueId(), callRequest.getAction(), response
                    });
        }
        return sendMessage;
    }
}
