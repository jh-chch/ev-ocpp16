package com.ev.ocpp16.web.controller.v1;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ev.ocpp16.application.ChargeInfoService;
import com.ev.ocpp16.web.dto.ChargerQueryDTO;
import com.ev.ocpp16.web.dto.ChargersQueryDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chargers")
@RequiredArgsConstructor
public class ChargerApiController {

    private final ChargeInfoService chargeInfoService;

    // 사이트의 모든 충전기 조회
    @GetMapping("")
    public ResponseEntity<ChargersQueryDTO.Response> getChargers(
            @Validated @ModelAttribute ChargersQueryDTO.Request request) {
        return ResponseEntity.ok(chargeInfoService.getChargers(request));
    }

    // 사이트의 특정 충전기 상세 조회
    @GetMapping("/{serialNumber}")
    public ResponseEntity<ChargerQueryDTO.Response> getCharger(
            @PathVariable(value = "serialNumber") @Length(max = 30) String serialNumber,
            @Validated @ModelAttribute ChargerQueryDTO.Request request) {
        return ResponseEntity.ok(chargeInfoService.getCharger(serialNumber, request));
    }

}
