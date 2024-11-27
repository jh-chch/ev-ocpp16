package com.ev.ocpp16.websocket.protocol.action;

import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;

public interface ActionHandler<T, R> {
    R handleAction(PathInfo pathInfo, CallRequest<T> callRequest) throws Exception;

    boolean validateRequiredFields(T payload);

    Class<T> getPayloadClass();
}
