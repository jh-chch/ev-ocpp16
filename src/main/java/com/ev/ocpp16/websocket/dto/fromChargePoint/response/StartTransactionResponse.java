package com.ev.ocpp16.websocket.dto.fromChargePoint.response;

import com.ev.ocpp16.websocket.dto.fromChargePoint.common.IdTagInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class StartTransactionResponse {
    private final IdTagInfo idTagInfo;
    private final Integer transactionId;
}
