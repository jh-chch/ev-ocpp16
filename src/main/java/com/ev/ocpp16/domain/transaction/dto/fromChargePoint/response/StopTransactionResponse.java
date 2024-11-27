package com.ev.ocpp16.domain.transaction.dto.fromChargePoint.response;

import com.ev.ocpp16.domain.common.dto.IdTagInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class StopTransactionResponse {
    private IdTagInfo idTagInfo;
}
