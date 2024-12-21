package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import org.springframework.stereotype.Component;

import com.ev.ocpp16.global.utils.DateTimeUtil;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.HeartbeatRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.HeartbeatResponse;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.RequiredArgsConstructor;

/**
 * 충전기 상태의 주기적인 신호
 */
@Component(USER_TYPE_USER + "Heartbeat")
@RequiredArgsConstructor
public class Heartbeat implements ActionHandler<HeartbeatRequest, HeartbeatResponse> {

    @Override
    public HeartbeatResponse handleAction(PathInfo pathInfo, CallRequest<HeartbeatRequest> callRequest) {
        return new HeartbeatResponse(DateTimeUtil.currentKoreanLocalDateTimeToISO8601());
    }

    @Override
    public boolean validateRequiredFields(HeartbeatRequest payload) {
        return true;
    }

    @Override
    public Class<HeartbeatRequest> getPayloadClass() {
        return HeartbeatRequest.class;
    }
}
