package com.ev.ocpp16.domain.chargepoint.entity;

import com.ev.ocpp16.domain.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "site")
@Getter
public class Site extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "city", length = 30)
    private String city;

    @Column(name = "street", length = 30)
    private String street;

    @Column(name = "zipcode", length = 30)
    private String zipcode;

}
