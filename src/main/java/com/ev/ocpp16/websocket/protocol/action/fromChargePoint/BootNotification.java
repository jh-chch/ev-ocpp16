package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ev.ocpp16.application.ChargingManageService;
import com.ev.ocpp16.domain.chargingManagement.dto.ChargerInfoUpdateRequestDTO;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerNotFoundException;
import com.ev.ocpp16.global.utils.DateTimeUtil;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.RegistrationStatus;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.BootNotificationRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.BootNotificationResponse;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 충전기가 부팅될 때, 정보 전송
 */
@Component(USER_TYPE_USER + "BootNotification")
@RequiredArgsConstructor
public class BootNotification implements ActionHandler<BootNotificationRequest, BootNotificationResponse> {

    private final ChargingManageService chargingManageService;

    @Value("${boot.interval}")
    private Integer interval;

    @PostConstruct
    public void init() {
        Assert.notNull(interval, "interval is not initialized");
        Assert.isTrue(interval > 0, "interval must be greater than 0");
    }

    @Override
    @Transactional
    public BootNotificationResponse handleAction(PathInfo pathInfo, CallRequest<BootNotificationRequest> callRequest) {
        RegistrationStatus registrationStatus = RegistrationStatus.Accepted;

        try {
            var chargerInfoUpdateDTO = ChargerInfoUpdateRequestDTO.builder()
                    .chargePointModel(callRequest.getPayload().getChargePointModel())
                    .chargePointVendor(callRequest.getPayload().getChargePointVendor())
                    .chargePointSerialNumber(callRequest.getPayload().getChargePointSerialNumber())
                    .firmwareVersion(callRequest.getPayload().getFirmwareVersion())
                    .build();

            chargingManageService.updateChargerInfo(pathInfo.getChargerIdentifier(), chargerInfoUpdateDTO);
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
