package com.ev.ocpp16.websocket.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ev.ocpp16.websocket.entity.enums.ChargeStep;

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
@Table(name = "charge_history_detail")
@Getter
@NoArgsConstructor
public class ChargeHistoryDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_history_detail_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_step", nullable = false)
    private ChargeStep chargeStep;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    @Column(name = "meter_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal meterValue;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_history_id", nullable = false)
    private ChargeHistory chargeHistory;

    public ChargeHistoryDetail(ChargeStep chargeStep, LocalDateTime actionDate, BigDecimal meterValue,
            BigDecimal unitPrice, ChargeHistory chargeHistory) {
        this.chargeStep = chargeStep;
        this.actionDate = actionDate;
        this.meterValue = meterValue;
        this.unitPrice = unitPrice;
        this.chargeHistory = chargeHistory;
    }

}
