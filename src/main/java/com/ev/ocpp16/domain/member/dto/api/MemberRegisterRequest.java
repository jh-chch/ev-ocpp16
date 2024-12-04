package com.ev.ocpp16.domain.member.dto.api;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.AccountStatus;
import com.ev.ocpp16.domain.member.entity.enums.Roles;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberRegisterRequest {
    @NotBlank
    private String idToken;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    public Member toEntity(Roles roles) {
        return new Member(idToken, username, password, email, roles, AccountStatus.ACTIVE);
    }
}
