package com.ev.ocpp16.domain.member.controller.api;

import static com.ev.ocpp16.domain.common.exception.ApiExceptionStatus.INVALID_AUTH_HEADER;

import java.util.Base64;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ev.ocpp16.config.security.JwtUtil;
import com.ev.ocpp16.domain.common.exception.ApiException;
import com.ev.ocpp16.domain.member.dto.api.MemberRegisterRequest;
import com.ev.ocpp16.domain.member.dto.api.MemberRegisterResponse;
import com.ev.ocpp16.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final MemberService memberService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> generateToken(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        final String BASIC_PREFIX = "Basic ";

        if (authorization == null || !authorization.startsWith(BASIC_PREFIX)) {
            throw new ApiException(INVALID_AUTH_HEADER);
        }

        var credentials = authorization.substring(BASIC_PREFIX.length());
        var decodedCredentials = new String(Base64.getDecoder().decode(credentials.trim()));    
        var parts = decodedCredentials.split(":", 2);

        if (parts.length != 2) {
            throw new ApiException(INVALID_AUTH_HEADER);
        }

        String email = parts[0];
        String password = parts[1];

        // 사용자 인증
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of("token", token));
    }

    // 일반 회원 회원가입
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@Validated @RequestBody MemberRegisterRequest request) {
        return ResponseEntity.ok(memberService.registerMember(request));
    }
}
