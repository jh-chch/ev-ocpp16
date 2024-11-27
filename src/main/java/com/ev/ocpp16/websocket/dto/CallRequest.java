package com.ev.ocpp16.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CallRequest<T> {
    private final Integer messageTypeId;
    private final String uniqueId;
    private String action;
    private final T payload;
}
