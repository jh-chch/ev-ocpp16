package com.ev.ocpp16.websocket;

import static com.ev.ocpp16.websocket.utils.Constants.CALL_CLIENT_TO_SERVER;
import static com.ev.ocpp16.websocket.utils.Constants.CALL_RESULT_SERVER_TO_CLIENT;
import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_ADMIN;
import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.ev.ocpp16.websocket.dto.CallResponse;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.dto.PathInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageSender {
    private final SessionManager sessionManager;

    public void sendMessage(CallResponse callResponse) throws IOException {
        String userType;

        switch (callResponse.getCallRequest().getMessageTypeId()) {
            case CALL_CLIENT_TO_SERVER:
                userType = USER_TYPE_USER;
                break;
            case CALL_RESULT_SERVER_TO_CLIENT:
                userType = USER_TYPE_ADMIN;
                break;
            default:
                throw new OcppException(
                        callResponse.getCallRequest().getUniqueId(),
                        ErrorCode.TYPE_CONSTRAINT_VIOLATION,
                        String.format("Invalid message type identifier for MessageTypeId: %s",
                                callResponse.getCallRequest().getMessageTypeId()));
        }

        broadcastMessage(callResponse, userType);
    }

    private void broadcastMessage(CallResponse callResponse, String userType) throws IOException {
        PathInfo pathInfo = callResponse.getPathInfo();
        boolean isSessionFound = false;

        for (WebSocketSession session : sessionManager.getSessionMap().values()) {
            PathInfo targetPathInfo = PathInfo.from(session);

            // 현재 세션이 타겟 세션이 맞으면 메시지 전송
            if (isTargetSession(userType, pathInfo, targetPathInfo)) {
                session.sendMessage(new TextMessage(callResponse.getSendMessage()));
                log.info("<==== Sent message {} session: {} targetSession: {}",
                        callResponse.getSendMessage(), callResponse.getSession(), session);

                isSessionFound = true;
                break;
            }
        }

        if (!isSessionFound) {
            throw new OcppException(
                    callResponse.getCallRequest().getUniqueId(),
                    ErrorCode.GENERIC_ERROR,
                    String.format("No matching session found for userType: %s and pathInfo: %s", userType, pathInfo));
        }
    }

    private boolean isTargetSession(String userType, PathInfo pathInfo, PathInfo targetPathInfo) {
        return userType.equals(targetPathInfo.getUserType())
                && pathInfo.getSiteId().equals(targetPathInfo.getSiteId())
                && pathInfo.getChgrId().equals(targetPathInfo.getChgrId());
    }
}
