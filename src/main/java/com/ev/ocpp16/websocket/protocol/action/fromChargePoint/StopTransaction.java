package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.TransactionDetailSaveDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.TransactionUpdateDTO;
import com.ev.ocpp16.domain.transaction.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.transaction.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.transaction.service.TransactionService;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.StopTransactionRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.StopTransactionResponse;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;
import com.ev.ocpp16.websocket.utils.DateTimeUtil;

import lombok.RequiredArgsConstructor;

/**
 * 충전 트랜잭션 종료
 */
@Component(USER_TYPE_USER + "StopTransaction")
@RequiredArgsConstructor
public class StopTransaction implements ActionHandler<StopTransactionRequest, StopTransactionResponse> {

    private final TransactionService transactionService;

    @Transactional
    @Override
    public StopTransactionResponse handleAction(PathInfo pathInfo, CallRequest<StopTransactionRequest> callRequest)
            throws ChargeHistoryNotFoundException, MemberNotFoundException, ChargerConnectorNotFoundException {

        StopTransactionRequest payload = callRequest.getPayload();
        Integer transactionId = payload.getTransactionId();
        LocalDateTime timestamp = DateTimeUtil.iso8601ToKoreanLocalDateTime(payload.getTimestamp());
        BigDecimal meterValue = new BigDecimal(payload.getMeterStop());
        ChargeStep chargeStep = ChargeStep.STOP_TRANSACTION;
        
        // 1. 충전 이력 업데이트
        TransactionUpdateDTO transactionUpdateDTO = TransactionUpdateDTO.builder()
                .transactionId(transactionId)
                .timestamp(timestamp)
                .meterValue(meterValue)
                .chargeStep(chargeStep)
                .build();

        transactionService.updateTransaction(transactionUpdateDTO);

        // 2. 충전 이력 상세 저장
        TransactionDetailSaveDTO transactionDetailSaveDTO = TransactionDetailSaveDTO.builder()
                .transactionId(transactionId)
                .timestamp(timestamp)
                .meterValue(meterValue)
                .chargeStep(chargeStep)
                .build();

        transactionService.saveTransactionDetail(transactionDetailSaveDTO);

        return new StopTransactionResponse();

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
