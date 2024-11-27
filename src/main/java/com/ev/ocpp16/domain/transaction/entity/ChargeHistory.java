package com.ev.ocpp16.domain.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ev.ocpp16.domain.chargepoint.entity.Charger;
import com.ev.ocpp16.domain.common.entity.BaseTimeEntity;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.transaction.entity.enums.ChargeStep;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "charge_history")
@Getter
@NoArgsConstructor
public class ChargeHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_history_id")
    private Integer id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "total_meter_value", precision = 10, scale = 2)
    private BigDecimal totalMeterValue;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_step", nullable = false)
    private ChargeStep chargeStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_id", nullable = false)
    private Charger charger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public ChargeHistory(LocalDateTime startTime, LocalDateTime endTime, BigDecimal totalMeterValue,
            BigDecimal totalPrice, ChargeStep chargeStep, Charger charger, Member member) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalMeterValue = totalMeterValue;
        this.totalPrice = totalPrice;
        this.chargeStep = chargeStep;
        this.charger = charger;
        this.member = member;
    }

    public void changeChgrHst(LocalDateTime endTime, BigDecimal totalMeterValue, ChargeStep chargeStep) {
        this.endTime = endTime;
        this.totalMeterValue = totalMeterValue;
        this.chargeStep = chargeStep;
    }
}
