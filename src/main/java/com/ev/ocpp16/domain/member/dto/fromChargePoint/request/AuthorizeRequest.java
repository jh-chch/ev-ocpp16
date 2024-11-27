package com.ev.ocpp16.domain.member.dto.fromChargePoint.request;

import com.ev.ocpp16.domain.common.dto.IdToken;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthorizeRequest {
    private IdToken idTag;
}