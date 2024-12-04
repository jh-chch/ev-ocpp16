package com.ev.ocpp16.domain.member.dto.api;

import static com.ev.ocpp16.domain.member.entity.enums.AccountStatus.ACTIVE;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.Address;
import com.ev.ocpp16.domain.member.entity.enums.Roles;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class MemberRegisterDTO {

    @Data
    public static class Request {
        @NotBlank
        private String email;

        @NotBlank
        private String username;

        @NotBlank
        private String password;

        @NotBlank
        private String idToken;

        @NotBlank
        private String phoneNumber;

        @NotBlank
        private String carNumber;

        @NotBlank
        private Address address;

        public Member toEntity(Roles roles) {
            return Member.builder()
                    .email(email)
                    .username(username)
                    .password(password)
                    .idToken(idToken)
                    .phoneNumber(phoneNumber)
                    .carNumber(carNumber)
                    .address(address)
                    .roles(roles)
                    .accountStatus(ACTIVE)
                    .build();
        }
    }

    @Data
    public static class Response {
        private final String idToken;
        private final String username;
        private final String email;
    }
}
