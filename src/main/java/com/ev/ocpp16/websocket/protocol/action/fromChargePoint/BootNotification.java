package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint.ChgrInfoUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.exception.ChargerNotFoundException;
import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.domain.common.dto.RegistrationStatus;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.BootNotificationRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.BootNotificationResponse;
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
        ChgrInfoUpdateDTO chgrInfoUpdateDTO = ChgrInfoUpdateDTO.builder()
                .chgrId(pathInfo.getChgrId())
                .model(callRequest.getPayload().getChargePointModel())
                .serialNumber(callRequest.getPayload().getChargePointSerialNumber())
                .vendor(callRequest.getPayload().getChargePointVendor())
                .firmwareVersion(callRequest.getPayload().getFirmwareVersion())
                .build();

        // 충전기 정보 업데이트 -> 충전기 정보 업데이트 실패 시 거절
        RegistrationStatus registrationStatus = RegistrationStatus.Accepted;
        try {
            chargerService.updateChgrInfo(chgrInfoUpdateDTO);
        } catch (ChargerNotFoundException e) {
            registrationStatus = RegistrationStatus.Rejected;
        }

        return new BootNotificationResponse(
                DateTimeUtil.currentKoreanLocalDateTimeToISO8601(),
                interval,
                registrationStatus);
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
