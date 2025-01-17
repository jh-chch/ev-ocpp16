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
import com.ev.ocpp16.application.MembershipService;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;
import com.ev.ocpp16.web.dto.MemberQueryDTO;
import com.ev.ocpp16.web.dto.MembersQueryDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MembershipService membershipService;
    private final ChargeInfoService chargeInfoService;

    // 회원 목록 조회
    @GetMapping("")
    public ResponseEntity<MembersQueryDTO.Response> getMembers(
            @Validated @ModelAttribute MembersQueryDTO.Request request) {
        return ResponseEntity.ok(membershipService.getMembers(request));
    }

    // 회원 조회
    @GetMapping("/{idToken}")
    public ResponseEntity<MemberQueryDTO.Response> getMemberByIdToken(
            @PathVariable("idToken") @Length(max = 20) String idToken) {
        return ResponseEntity.ok(membershipService.getMemberByIdToken(idToken));
    }

    // 회원 충전 이력 조회
    @GetMapping("/{idToken}/charge-history")
    public ResponseEntity<ChargeHistoryQueryDTO.Response> getChargeHistory(
            @PathVariable("idToken") @Length(max = 20) String idToken,
            @Validated @ModelAttribute ChargeHistoryQueryDTO.Request request) {
        return ResponseEntity.ok(chargeInfoService.getChargeHistory(idToken, request));
    }
}
