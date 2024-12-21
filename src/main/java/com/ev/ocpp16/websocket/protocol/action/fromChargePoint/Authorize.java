package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import org.springframework.stereotype.Component;

import com.ev.ocpp16.application.ChargingManageService;
import com.ev.ocpp16.domain.member.exception.MemberException;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.AuthorizationStatus;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.IdTagInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.AuthorizeRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.AuthorizeResponse;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.RequiredArgsConstructor;

/**
 * 충전 시작 또는 중지 전, idTag를 검증
 */
@Component(USER_TYPE_USER + "Authorize")
@RequiredArgsConstructor
public class Authorize implements ActionHandler<AuthorizeRequest, AuthorizeResponse> {

    private final ChargingManageService chargingManageService;

    @Override
    public AuthorizeResponse handleAction(PathInfo pathInfo, CallRequest<AuthorizeRequest> callRequest) {
        try {
            String idToken = callRequest.getPayload().getIdTag().getIdToken();
            chargingManageService.validateMemberForCharging(idToken);
            return new AuthorizeResponse(new IdTagInfo(AuthorizationStatus.Accepted));
        } catch (MemberNotFoundException e) {
            return new AuthorizeResponse(new IdTagInfo(AuthorizationStatus.Invalid));
        } catch (MemberException e) {
            return new AuthorizeResponse(new IdTagInfo(AuthorizationStatus.Blocked));
        }
    }

    @Override
    public boolean validateRequiredFields(AuthorizeRequest payload) {
        if (payload.getIdTag() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Class<AuthorizeRequest> getPayloadClass() {
        return AuthorizeRequest.class;
    }
}
