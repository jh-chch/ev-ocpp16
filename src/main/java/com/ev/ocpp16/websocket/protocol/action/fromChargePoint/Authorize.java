package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import org.springframework.stereotype.Component;

import com.ev.ocpp16.domain.common.dto.AuthorizationStatus;
import com.ev.ocpp16.domain.common.dto.IdTagInfo;
import com.ev.ocpp16.domain.member.dto.fromChargePoint.request.AuthorizeRequest;
import com.ev.ocpp16.domain.member.dto.fromChargePoint.response.AuthorizeResponse;
import com.ev.ocpp16.domain.member.entity.enums.AccountStatus;
import com.ev.ocpp16.domain.member.service.MemberService;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.RequiredArgsConstructor;

/**
 * 충전 시작 또는 중지 전, idTag를 검증
 */
@Component(USER_TYPE_USER + "Authorize")
@RequiredArgsConstructor
public class Authorize implements ActionHandler<AuthorizeRequest, AuthorizeResponse> {

    private final MemberService memberService;

    @Override
    public AuthorizeResponse handleAction(PathInfo pathInfo, CallRequest<AuthorizeRequest> callRequest) {
        String idToken = callRequest.getPayload().getIdTag().getIdToken();

        // idToken으로 회원 조회
        return memberService.getMemberByIdToken(idToken)
                .map(member -> {
                    AuthorizationStatus status = AccountStatus.ACTIVE.equals(member.getAccountStatus())
                            ? AuthorizationStatus.Accepted
                            : AuthorizationStatus.Blocked;
                    return new AuthorizeResponse(new IdTagInfo(status));
                })
                .orElseGet(() -> new AuthorizeResponse(new IdTagInfo(AuthorizationStatus.Invalid)));
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
