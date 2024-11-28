package com.ev.ocpp16.domain.member.dto.fromChargePoint.request;

import com.ev.ocpp16.domain.common.dto.IdToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthorizeRequest {
    private final IdToken idTag;

    @JsonCreator
    public AuthorizeRequest(@JsonProperty("idTag") IdToken idTag) {
        this.idTag = idTag;
    }
}