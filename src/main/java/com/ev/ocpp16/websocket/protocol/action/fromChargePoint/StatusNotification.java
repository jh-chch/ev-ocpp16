package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import org.springframework.stereotype.Component;

import com.ev.ocpp16.application.ChargingManageService;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerException;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.StatusNotificationRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.StatusNotificationResponse;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 충전기 커넥터 상태 변경이나 오류 알림
 */
@Slf4j
@Component(USER_TYPE_USER + "StatusNotification")
@RequiredArgsConstructor
public class StatusNotification implements ActionHandler<StatusNotificationRequest, StatusNotificationResponse> {

    private final ChargingManageService chargingManageService;

    @Override
    public StatusNotificationResponse handleAction(PathInfo pathInfo,
            CallRequest<StatusNotificationRequest> callRequest) {
        try {
            chargingManageService.processChargerStatusNotification(
                    pathInfo.getChargerIdentifier(),
                    callRequest.getPayload().getConnectorId(),
                    callRequest.getPayload().getStatus().toConnectorStatus(),
                    callRequest.getPayload().getErrorCode().toChargerErrorCode());
            return new StatusNotificationResponse();
        } catch (ChargerException e) {
            return new StatusNotificationResponse();
        }
    }

    @Override
    public boolean validateRequiredFields(StatusNotificationRequest payload) {
        if (payload.getConnectorId() == null || payload.getConnectorId() < 0
                || payload.getErrorCode() == null || payload.getStatus() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Class<StatusNotificationRequest> getPayloadClass() {
        return StatusNotificationRequest.class;
    }
}
