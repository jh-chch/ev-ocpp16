package com.ev.ocpp16.domain.member.dto.fromChargePoint;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.AccountStatus;
import com.ev.ocpp16.domain.member.entity.enums.Roles;

import lombok.Getter;

@Getter
public class ChargingMemberDTO {
    private String idToken;
    private String username;
    private String email;
    private Roles roles;
    private AccountStatus accountStatus;

    public ChargingMemberDTO(Member member) {
        this.idToken = member.getIdToken();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.roles = member.getRoles();
        this.accountStatus = member.getAccountStatus();
    }
}
