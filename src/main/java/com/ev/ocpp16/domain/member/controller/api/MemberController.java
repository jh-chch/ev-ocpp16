package com.ev.ocpp16.domain.member.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/v1/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
}
