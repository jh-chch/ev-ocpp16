package com.ev.ocpp16.domain.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;
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

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "total_meter_value", precision = 10, scale = 2)
    private BigDecimal totalMeterValue;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_step", nullable = false)
    private ChargeStep chargeStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_connector_id", nullable = false)
    private ChargerConnector chargerConnector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public ChargeHistory(LocalDateTime startDatetime, LocalDateTime endDatetime, BigDecimal totalMeterValue,
            BigDecimal totalPrice, ChargeStep chargeStep, ChargerConnector chargerConnector, Member member) {
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.totalMeterValue = totalMeterValue;
        this.totalPrice = totalPrice;
        this.chargeStep = chargeStep;
        this.chargerConnector = chargerConnector;
        this.member = member;
    }

    public void changeChgrHst(LocalDateTime endDatetime, ChargeStep chargeStep) {
        this.endDatetime = endDatetime;
        this.chargeStep = chargeStep;
    }

    public void calculateTotalMeterValue(BigDecimal newMeterValue, BigDecimal lastMeterValue) {
        this.totalMeterValue = this.totalMeterValue.add(newMeterValue).subtract(lastMeterValue);
    }
}
