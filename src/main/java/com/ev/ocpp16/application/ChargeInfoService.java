package com.ev.ocpp16.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargingManagement.dto.ChargerQueryDTO;
import com.ev.ocpp16.domain.chargingManagement.dto.ChargersQueryDTO;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;
import com.ev.ocpp16.domain.chargingManagement.service.ChargerQueryService;
import com.ev.ocpp16.domain.chargingManagement.service.HistoryQueryService;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;

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
        return ChargersQueryDTO.Response.of(chargerQueryService.getChargers(request.getSiteName()));
    }

    /**
     * 사이트의 특정 충전기 상세 조회
     * 
     * @param serialNumber 충전기 시리얼 번호
     * @param request      조회 조건
     * @return 충전기 상세
     */
    public ChargerQueryDTO.Response getCharger(String serialNumber, ChargerQueryDTO.Request request) {
        return ChargerQueryDTO.Response.of(
                chargerQueryService.getCharger(serialNumber, request.getSiteName()).orElse(null));
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
        return ChargeHistoryQueryDTO.Response.of(memberChargeHistories);
    }
}