package com.ev.ocpp16.domain.member.dto.api;

import static com.ev.ocpp16.domain.member.entity.enums.AccountStatus.ACTIVE;

import org.hibernate.validator.constraints.Length;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.Address;
import com.ev.ocpp16.domain.member.entity.enums.Roles;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class MemberSaveDTO {

    @Data
    public static class Request {
        @Email
        @NotBlank
        @Length(max = 100)
        private String email;

        @NotBlank
        @Length(max = 30)
        private String username;

        @NotBlank
        @Length(max = 100)
        private String password;

        @NotBlank
        @Length(max = 36)
        private String idToken;

        @NotBlank
        @Length(max = 15)
        private String phoneNumber;

        @NotBlank
        @Length(max = 15)
        private String carNumber;

        @Valid
        @NotNull
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
