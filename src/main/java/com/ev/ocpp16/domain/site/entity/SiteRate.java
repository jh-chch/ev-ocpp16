package com.ev.ocpp16.domain.site.entity;

import java.math.BigDecimal;

import com.ev.ocpp16.domain.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "site_rate")
@Getter
public class SiteRate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_rate_id")
    private Integer id;

    @Column(name = "hour", nullable = false)
    
    private int hour;
    @Column(name = "rate_per_wh", nullable = false, columnDefinition = "decimal(5,2) default 0.10")
    private BigDecimal ratePerWh;

    @Column(name = "member_discount", columnDefinition = "decimal(5,2)")
    private BigDecimal memberDiscount;

    @Column(name = "non_member_discount", columnDefinition = "decimal(5,2)")
    private BigDecimal nonMemberDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;
}
