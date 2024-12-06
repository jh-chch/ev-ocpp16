package com.ev.ocpp16.domain.member.controller.api;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ev.ocpp16.domain.member.dto.api.MemberQueryDTO;
import com.ev.ocpp16.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 조회
     * GET /api/v1/members/{idToken}
     */
    @GetMapping("/{idToken}")
    public ResponseEntity<MemberQueryDTO.Response> getMember(@PathVariable @Length(max = 36) String idToken) {
        return ResponseEntity.ok(memberService.getMemberByIdToken(idToken));
    }

}
