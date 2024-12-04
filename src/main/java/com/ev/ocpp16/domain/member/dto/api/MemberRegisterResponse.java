package com.ev.ocpp16.domain.member.dto.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberRegisterResponse {
    private final String idToken;
    private final String username;
    private final String email;
}
