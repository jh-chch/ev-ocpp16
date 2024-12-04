package com.ev.ocpp16.domain.member.entity.enums;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Embeddable
public class Address {
    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "address1", length = 100)
    private String address1;

    @Column(name = "address2", length = 100)
    private String address2;
}
