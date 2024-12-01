package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.common.dto.Measurand;
import com.ev.ocpp16.domain.common.dto.MeterValue;
import com.ev.ocpp16.domain.common.dto.SampledValue;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.SaveTransactionDetailDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.UpdateTransactionDTO;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.request.MeterValuesRequest;
import com.ev.ocpp16.domain.transaction.dto.fromChargePoint.response.MeterValuesResponse;
import com.ev.ocpp16.domain.transaction.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.transaction.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.transaction.service.TransactionService;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;
import com.ev.ocpp16.websocket.utils.DateTimeUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 충전 트랜잭션 진행
 */
@Component(USER_TYPE_USER + "MeterValues")
@RequiredArgsConstructor
public class MeterValues implements ActionHandler<MeterValuesRequest, MeterValuesResponse> {

    private final TransactionService transactionService;

    @Transactional
    @Override
    public MeterValuesResponse handleAction(PathInfo pathInfo, CallRequest<MeterValuesRequest> callRequest)
            throws MemberNotFoundException, ChargerConnectorNotFoundException, ChargeHistoryNotFoundException {
        // MeterValue 추출
        MeterValueData meterValueData = extractMeterValueData(callRequest);
        MeterValuesRequest payload = callRequest.getPayload();
        LocalDateTime timestamp = DateTimeUtil.iso8601ToBasicDateTime(meterValueData.getTimestamp());
        BigDecimal meterValue = BigDecimal.valueOf(Double.parseDouble(meterValueData.getValue()));

        // 1. 충전 이력 업데이트
        UpdateTransactionDTO updateTransactionDTO = new UpdateTransactionDTO(
                payload.getTransactionId(),
                timestamp,
                meterValue,
                ChargeStep.METER_VALUES);

        transactionService.updateTransaction(updateTransactionDTO);

        // 2. 충전 이력 상세 저장
        SaveTransactionDetailDTO saveTransactionDetailDTO = new SaveTransactionDetailDTO(
                payload.getTransactionId(),
                timestamp,
                meterValue,
                ChargeStep.METER_VALUES);

        transactionService.saveTransactionDetail(saveTransactionDetailDTO);

        return new MeterValuesResponse();
    }

    @Override
    public boolean validateRequiredFields(MeterValuesRequest payload) {
        if (payload.getConnectorId() == null || payload.getConnectorId() < 0 || payload.getMeterValue() == null) {
            return false;
        }
        for (MeterValue meterValue : payload.getMeterValue()) {
            if (meterValue.getTimestamp() == null || meterValue.getSampledValue() == null) {
                return false;
            }

            for (SampledValue sampledValue : meterValue.getSampledValue()) {
                if (sampledValue.getValue() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Class<MeterValuesRequest> getPayloadClass() {
        return MeterValuesRequest.class;
    }

    private MeterValueData extractMeterValueData(CallRequest<MeterValuesRequest> callRequest) {
        String value = null;
        String unit = null;
        String timestamp = null;

        for (MeterValue meterValue : callRequest.getPayload().getMeterValue()) {
            timestamp = meterValue.getTimestamp();
            for (SampledValue sampledValue : meterValue.getSampledValue()) {
                Measurand measurand = sampledValue.getMeasurand();
                if (measurand == null
                        || Measurand.ENERGY_ACTIVE_IMPORT_REGISTER.getValue().equals(measurand.getValue())) {
                    value = sampledValue.getValue();
                    unit = (measurand == null) ? "WH" : sampledValue.getUnit().name();
                    break;
                }
            }
        }

        if (!"WH".equalsIgnoreCase(unit)) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.OCCURENCE_CONSTRAINT_VIOLATION,
                    "Charging unit is not WH");
        }

        return new MeterValueData(value, unit, timestamp);
    }

    @Getter
    @AllArgsConstructor
    private static class MeterValueData {
        private final String value;
        private final String unit;
        private final String timestamp;
    }
}
