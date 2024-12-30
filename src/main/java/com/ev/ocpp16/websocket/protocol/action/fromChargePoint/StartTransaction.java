package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.application.ChargingManageService;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerException;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.global.utils.DateTimeUtil;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.AuthorizationStatus;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.IdTagInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.StartTransactionRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.StartTransactionResponse;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.RequiredArgsConstructor;

/**
 * 충전 트랜잭션 시작
 */
@Component(USER_TYPE_USER + "StartTransaction")
@RequiredArgsConstructor
public class StartTransaction implements ActionHandler<StartTransactionRequest, StartTransactionResponse> {

    private final ChargingManageService chargingManageService;

    @Transactional
    @Override
    public StartTransactionResponse handleAction(PathInfo pathInfo, CallRequest<StartTransactionRequest> callRequest) {

        try {
            Integer transactionId = chargingManageService.processStartTransaction(
                    pathInfo.getChargerIdentifier(),
                    callRequest.getPayload().getConnectorId(),
                    callRequest.getPayload().getIdTag(),
                    DateTimeUtil.iso8601ToKoreanLocalDateTime(callRequest.getPayload().getTimestamp()),
                    BigDecimal.valueOf(callRequest.getPayload().getMeterStart()));

            return new StartTransactionResponse(new IdTagInfo(AuthorizationStatus.Accepted), transactionId);
        } catch (ChargerException | MemberNotFoundException e) {
            return new StartTransactionResponse(new IdTagInfo(AuthorizationStatus.Invalid), -1);
        }

    }

    @Override
    public boolean validateRequiredFields(StartTransactionRequest payload) {
        if (payload.getConnectorId() == null || payload.getConnectorId() <= 0 || payload.getIdTag() == null
                || payload.getMeterStart() == null || payload.getTimestamp() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Class<StartTransactionRequest> getPayloadClass() {
        return StartTransactionRequest.class;
    }
}
