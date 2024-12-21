package com.ev.ocpp16.web.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ev.ocpp16.application.ChargeInfoService;
import com.ev.ocpp16.application.MembershipService;
import com.ev.ocpp16.domain.member.dto.MemberQueryDTO;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberApiControllerV1 {

    private final MembershipService membershipService;
    private final ChargeInfoService chargeInfoService;

    // 회원 조회
    @GetMapping("/{idToken}")
    public ResponseEntity<MemberQueryDTO.Response> getMemberByIdToken(
            @PathVariable @Length(max = 36) String idToken) {
        return ResponseEntity.ok(membershipService.getMember(idToken));
    }

    // 회원 충전 이력 조회
    @GetMapping("/{idToken}/charge-history")
    public ResponseEntity<ChargeHistoryQueryDTO.Response> getChargeHistory(
            @PathVariable @Length(max = 36) String idToken,
            @Validated @ModelAttribute ChargeHistoryQueryDTO.Request request) {
        return ResponseEntity.ok(chargeInfoService.getChargeHistory(idToken, request));
    }
}
