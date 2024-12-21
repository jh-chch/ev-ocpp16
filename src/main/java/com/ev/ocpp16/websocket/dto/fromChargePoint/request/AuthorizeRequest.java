package com.ev.ocpp16.websocket.dto.fromChargePoint.request;

import com.ev.ocpp16.websocket.dto.fromChargePoint.common.IdToken;
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