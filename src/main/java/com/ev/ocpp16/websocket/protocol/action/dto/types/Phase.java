package com.ev.ocpp16.websocket.protocol.action.dto.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Phase {
    @JsonProperty("L1")
    L1("L1"),

    @JsonProperty("L2")
    L2("L2"),

    @JsonProperty("L3")
    L3("L3"),

    @JsonProperty("N")
    N("N"),

    @JsonProperty("L1-N")
    L1_N("L1-N"),

    @JsonProperty("L2-N")
    L2_N("L2-N"),

    @JsonProperty("L3-N")
    L3_N("L3-N"),

    @JsonProperty("L1-L2")
    L1_L2("L1-L2"),

    @JsonProperty("L2-L3")
    L2_L3("L2-L3"),

    @JsonProperty("L3-L1")
    L3_L1("L3-L1");

    private String value;
}
