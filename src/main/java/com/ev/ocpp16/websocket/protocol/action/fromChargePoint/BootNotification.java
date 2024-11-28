package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ev.ocpp16.domain.chargepoint.dto.ChgrInfoUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint.request.BootNotificationRequest;
import com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint.response.BootNotificationResponse;
import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.domain.common.dto.RegistrationStatus;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;
import com.ev.ocpp16.websocket.utils.DateTimeUtil;

import lombok.RequiredArgsConstructor;

/**
 * 충전기가 부팅될 때, 정보 전송
 */
@Component(USER_TYPE_USER + "BootNotification")
@RequiredArgsConstructor
public class BootNotification implements ActionHandler<BootNotificationRequest, BootNotificationResponse> {

    private final ChargerService chargerService;

    @Value("${boot.interval}")
    private Integer interval;

    @Override
    public BootNotificationResponse handleAction(PathInfo pathInfo, CallRequest<BootNotificationRequest> callRequest) {
        boolean updateChgrInfo = chargerService.updateChgrInfo(
                new ChgrInfoUpdateDTO(pathInfo.getChgrId(),
                        callRequest.getPayload().getChargePointModel(),
                        callRequest.getPayload().getChargePointSerialNumber(),
                        callRequest.getPayload().getChargePointVendor(),
                        callRequest.getPayload().getFirmwareVersion()));

        RegistrationStatus registrationStatus = updateChgrInfo ? RegistrationStatus.Accepted
                : RegistrationStatus.Rejected;

        return new BootNotificationResponse(DateTimeUtil.currentDateTimeToISO8601(), interval, registrationStatus);
    }

    @Override
    public boolean validateRequiredFields(BootNotificationRequest payload) {
        if (payload.getChargePointModel() == null || payload.getChargePointVendor() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Class<BootNotificationRequest> getPayloadClass() {
        return BootNotificationRequest.class;
    }

}
