package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.common.dto.AuthorizationStatus;
import com.ev.ocpp16.domain.common.dto.IdTagInfo;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.TransactionDetailSaveDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.TransactionSaveDTO;
import com.ev.ocpp16.domain.transaction.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.transaction.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.transaction.service.TransactionService;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.StartTransactionRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.StartTransactionResponse;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;
import com.ev.ocpp16.websocket.utils.DateTimeUtil;

import lombok.RequiredArgsConstructor;

/**
 * 충전 트랜잭션 시작
 */
@Component(USER_TYPE_USER + "StartTransaction")
@RequiredArgsConstructor
public class StartTransaction implements ActionHandler<StartTransactionRequest, StartTransactionResponse> {

    private final TransactionService transactionService;

    @Transactional
    @Override
    public StartTransactionResponse handleAction(PathInfo pathInfo, CallRequest<StartTransactionRequest> callRequest)
            throws ChargerConnectorNotFoundException, MemberNotFoundException, ChargeHistoryNotFoundException {

        StartTransactionRequest payload = callRequest.getPayload();
        String idTag = payload.getIdTag();
        Long chgrId = pathInfo.getChgrId();
        Integer connectorId = payload.getConnectorId();
        LocalDateTime iso8601ToKoreanLocalDateTime = DateTimeUtil.iso8601ToKoreanLocalDateTime(payload.getTimestamp());
        BigDecimal meterValue = BigDecimal.valueOf(payload.getMeterStart());
        ChargeStep chargeStep = ChargeStep.START_TRANSACTION;

        // 충전기 상태 검사
        if (!transactionService.isChgrStValidForStartCharging(chgrId, connectorId)) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.GENERIC_ERROR, "Unable to start charging");
        }

        // 충전 이력 저장
        TransactionSaveDTO transactionSaveDTO = TransactionSaveDTO.builder()
                .idToken(idTag)
                .chgrId(chgrId)
                .connectorId(connectorId)
                .timestamp(iso8601ToKoreanLocalDateTime)
                .meterValue(BigDecimal.ZERO)
                .chargeStep(chargeStep)
                .build();

        Integer transactionId = transactionService.saveTransaction(transactionSaveDTO);

        // 충전 이력 상세 저장
        TransactionDetailSaveDTO transactionDetailSaveDTO = TransactionDetailSaveDTO.builder()
                .transactionId(transactionId)
                .timestamp(iso8601ToKoreanLocalDateTime)
                .meterValue(meterValue)
                .chargeStep(chargeStep)
                .build();

        transactionService.saveTransactionDetail(transactionDetailSaveDTO);

        return new StartTransactionResponse(new IdTagInfo(AuthorizationStatus.Accepted), transactionId);
    }

    @Override
    public boolean validateRequiredFields(StartTransactionRequest payload) {
        if (payload.getConnectorId() == null || payload.getConnectorId() <= 0 || payload.getIdTag() == null
                || payload.getMeterStart() == null || payload.getTimestamp() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Class<StartTransactionRequest> getPayloadClass() {
        return StartTransactionRequest.class;
    }
}
