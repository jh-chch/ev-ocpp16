package com.ev.ocpp16.domain.member.entity.enums;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {
    @NotBlank
    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "address1", length = 100)
    private String address1;

    @Column(name = "address2", length = 100)
    private String address2;

    @Override
    public String toString() {
        return new StringBuilder()
                .append(zipCode)
                .append(" ")
                .append(address1 == null ? "" : address1)
                .append(" ")
                .append(address2 == null ? "" : address2)
                .toString();
    }
}
