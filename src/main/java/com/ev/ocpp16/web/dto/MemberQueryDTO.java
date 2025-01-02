package com.ev.ocpp16.web.dto;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.AccountStatus;
import com.ev.ocpp16.domain.member.entity.enums.Address;
import com.ev.ocpp16.domain.member.entity.enums.Roles;

import lombok.Data;

public class MemberQueryDTO {

    @Data
    public static class Response {
        private final String email;
        private final String username;
        private final String idToken;
        private final String phoneNumber;
        private final String carNumber;
        private final Address address;
        private final Roles role;
        private final AccountStatus accountStatus;

        public Response(Member member) {
            this.email = member.getEmail();
            this.username = member.getUsername();
            this.idToken = member.getIdToken();
            this.phoneNumber = member.getPhoneNumber();
            this.carNumber = member.getCarNumber();
            this.address = member.getAddress();
            this.role = member.getRoles();
            this.accountStatus = member.getAccountStatus();
        }
    }
}
