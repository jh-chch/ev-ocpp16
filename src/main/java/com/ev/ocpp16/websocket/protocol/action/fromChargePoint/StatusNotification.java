package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import org.springframework.stereotype.Component;

import com.ev.ocpp16.domain.chargepoint.dto.ChgrConnUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.dto.ChgrErrorHstSaveDTO;
import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.domain.common.dto.ChargePointErrorCode;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.request.StatusNotificationRequest;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.response.StatusNotificationResponse;
import com.ev.ocpp16.domain.transaction.service.TransactionService;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 상태 변경이나 오류 알림
 */
@Slf4j
@Component(USER_TYPE_USER + "StatusNotification")
@RequiredArgsConstructor
public class StatusNotification implements ActionHandler<StatusNotificationRequest, StatusNotificationResponse> {

    private final ChargerService chargerService;
    private final TransactionService transactionService;

    @Override
    public StatusNotificationResponse handleAction(PathInfo pathInfo,
            CallRequest<StatusNotificationRequest> callRequest) {
        Long chgrId = pathInfo.getChgrId();
        Integer connectorId = callRequest.getPayload().getConnectorId();
        ChargePointErrorCode errorCode = callRequest.getPayload().getErrorCode();

        // 1. 충전기 커넥터 상태값 변경
        chargerService.updateChgrConn(new ChgrConnUpdateDTO(chgrId, connectorId, callRequest.getPayload().getStatus()))
                .orElseThrow(() -> new OcppException(callRequest.getUniqueId(), ErrorCode.GENERIC_ERROR,
                        "ChargerConnector not found: " + chgrId + ", " + connectorId));

        // 2. 에러 이력 저장
        transactionService.saveChgrErrorHst(
                new ChgrErrorHstSaveDTO(chgrId, connectorId, errorCode, callRequest.getPayload().getInfo()));

        return new StatusNotificationResponse();
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
