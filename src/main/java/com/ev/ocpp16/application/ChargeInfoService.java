package com.ev.ocpp16.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargingManagement.dto.ChargerQueryDTO;
import com.ev.ocpp16.domain.chargingManagement.dto.ChargersQueryDTO;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;
import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerException;
import com.ev.ocpp16.domain.chargingManagement.service.ChargerQueryService;
import com.ev.ocpp16.domain.chargingManagement.service.HistoryQueryService;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;
import com.ev.ocpp16.web.exception.ApiException;
import com.ev.ocpp16.web.exception.ApiExceptionStatus;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChargeInfoService {

    private final ChargerQueryService chargerQueryService;
    private final HistoryQueryService historyQueryService;

    /**
     * 사이트의 모든 충전기 조회
     * 
     * @param request 조회 조건
     * @return 충전기 목록
     */
    public ChargersQueryDTO.Response getChargers(ChargersQueryDTO.Request request) {
        try {
            List<Charger> chargers = chargerQueryService.getChargers(request.getSiteName());

            if (chargers.isEmpty()) {
                throw new ApiException(ApiExceptionStatus.NOT_FOUND_CHARGER);
            }

            return ChargersQueryDTO.Response.of(chargers);
        } catch (ChargerException e) {
            throw new ApiException(ApiExceptionStatus.INVALID_VALUE, e.getMessage());
        }
    }

    /**
     * 사이트의 특정 충전기 상세 조회
     * 
     * @param serialNumber 충전기 시리얼 번호
     * @param request      조회 조건
     * @return 충전기 상세
     */
    public ChargerQueryDTO.Response getCharger(String serialNumber, ChargerQueryDTO.Request request) {
        try {
            return ChargerQueryDTO.Response.of(
                    chargerQueryService.getCharger(serialNumber, request.getSiteName())
                            .orElseThrow(() -> new ApiException(ApiExceptionStatus.NOT_FOUND_CHARGER)));
        } catch (ChargerException e) {
            throw new ApiException(ApiExceptionStatus.INVALID_VALUE, e.getMessage());
        }
    }

    /**
     * 회원의 충전 이력 조회
     * 
     * @param idToken 회원 식별자
     * @param request 조회 조건
     * @return 충전 이력 목록
     */
    public ChargeHistoryQueryDTO.Response getChargeHistory(String idToken,
            ChargeHistoryQueryDTO.Request request) {
        List<ChargeHistory> memberChargeHistories = historyQueryService.getMemberChargeHistory(
                idToken,
                request.getStartDatetime(),
                request.getEndDatetime());

        if (memberChargeHistories.isEmpty()) {
            throw new ApiException(ApiExceptionStatus.NOT_FOUND_CHARGE_HISTORY);
        }

        return ChargeHistoryQueryDTO.Response.of(memberChargeHistories);
    }
}