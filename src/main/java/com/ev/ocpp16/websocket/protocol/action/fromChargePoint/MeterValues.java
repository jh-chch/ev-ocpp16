package com.ev.ocpp16.websocket.protocol.action.fromChargePoint;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.application.ChargingManageService;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryException;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.site.exception.SiteRateException;
import com.ev.ocpp16.global.utils.DateTimeUtil;
import com.ev.ocpp16.websocket.dto.CallRequest;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.Measurand;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.MeterValue;
import com.ev.ocpp16.websocket.dto.fromChargePoint.common.SampledValue;
import com.ev.ocpp16.websocket.dto.fromChargePoint.request.MeterValuesRequest;
import com.ev.ocpp16.websocket.dto.fromChargePoint.response.MeterValuesResponse;
import com.ev.ocpp16.websocket.exception.ErrorCode;
import com.ev.ocpp16.websocket.exception.OcppException;
import com.ev.ocpp16.websocket.protocol.action.ActionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 충전 트랜잭션 진행
 */
@Component(USER_TYPE_USER + "MeterValues")
@RequiredArgsConstructor
public class MeterValues implements ActionHandler<MeterValuesRequest, MeterValuesResponse> {

    private final ChargingManageService chargingManageService;

    @Transactional
    @Override
    public MeterValuesResponse handleAction(PathInfo pathInfo, CallRequest<MeterValuesRequest> callRequest) {
        // MeterValue 추출
        MeterValueData meterValueData = extractMeterValueData(callRequest);

        try {
            chargingManageService.processMeterValues(
                    callRequest.getPayload().getTransactionId(),
                    DateTimeUtil.iso8601ToKoreanLocalDateTime(meterValueData.getTimestamp()),
                    BigDecimal.valueOf(Double.parseDouble(meterValueData.getValue())),
                    ChargeStep.METER_VALUES);

            return new MeterValuesResponse();
        } catch (ChargeHistoryNotFoundException e) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.OCCURENCE_CONSTRAINT_VIOLATION,
                    "충전 이력을 찾을 수 없습니다.");
        } catch (ChargeHistoryException e) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.TYPE_CONSTRAINT_VIOLATION, e.getMessage());
        } catch (SiteRateException e) {
            throw new OcppException(callRequest.getUniqueId(), ErrorCode.GENERIC_ERROR, e.getMessage());
        }

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
