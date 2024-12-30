package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.application.ChargingManageService;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryException;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.site.exception.SiteRateException;
import com.ev.ocpp16.global.utils.DateTimeUtil;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.StopTransactionRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.StopTransactionResponse;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.RequiredArgsConstructor;

/**
 * 충전 트랜잭션 종료
 */
@Component(USER_TYPE_USER + "StopTransaction")
@RequiredArgsConstructor
public class StopTransaction implements ActionHandler<StopTransactionRequest, StopTransactionResponse> {

    private final ChargingManageService chargingManageService;

    @Transactional
    @Override
    public StopTransactionResponse handleAction(PathInfo pathInfo, CallRequest<StopTransactionRequest> callRequest) {
        StopTransactionRequest payload = callRequest.getPayload();

        try {
            chargingManageService.processMeterValues(
                    payload.getTransactionId(),
                    DateTimeUtil.iso8601ToKoreanLocalDateTime(payload.getTimestamp()),
                    new BigDecimal(payload.getMeterStop()),
                    ChargeStep.STOP_TRANSACTION);
            return new StopTransactionResponse();
        } catch (ChargeHistoryNotFoundException e) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.OCCURENCE_CONSTRAINT_VIOLATION,
                    "충전 이력을 찾을 수 없습니다.");
        } catch (ChargeHistoryException e) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.TYPE_CONSTRAINT_VIOLATION, e.getMessage());
        } catch (SiteRateException e) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.GENERIC_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean validateRequiredFields(StopTransactionRequest payload) {
        if (payload.getMeterStop() == null || payload.getTimestamp() == null || payload.getTransactionId() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Class<StopTransactionRequest> getPayloadClass() {
        return StopTransactionRequest.class;
    }

}
