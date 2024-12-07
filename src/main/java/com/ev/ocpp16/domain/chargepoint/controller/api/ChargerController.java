package com.ev.ocpp16.domain.chargepoint.controller.api;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrQueryDTO;
import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrsQueryDTO;
import com.ev.ocpp16.domain.chargepoint.service.ChargerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chargers")
@RequiredArgsConstructor
public class ChargerController {

    private final ChargerService chargerService;

    /**
     * 사이트의 모든 충전기 조회
     * GET /api/v1/chargers?siteName={siteName}
     */
    @GetMapping("")
    public ResponseEntity<ChgrsQueryDTO.Response> getChgrsBySite(
            @Validated @ModelAttribute ChgrsQueryDTO.Request request) {
        return ResponseEntity.ok(chargerService.getChgrsBySite(request));
    }

    /**
     * 사이트의 특정 충전기 상세 조회
     * GET /api/v1/chargers/{serialNumber}?siteName={siteName}
     */
    @GetMapping("/{serialNumber}")
    public ResponseEntity<ChgrQueryDTO.Response> getChgrBySite(
            @PathVariable(value = "serialNumber") @Length(max = 30) String serialNumber,
            @Validated @ModelAttribute ChgrQueryDTO.Request request) {
        return ResponseEntity.ok(chargerService.getChgrBySite(serialNumber, request));
    }

}
