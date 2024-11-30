package com.ev.ocpp16.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends Exception {

    public MemberNotFoundException(String idToken) {
        super("Member not found: " + idToken);
    }
}
