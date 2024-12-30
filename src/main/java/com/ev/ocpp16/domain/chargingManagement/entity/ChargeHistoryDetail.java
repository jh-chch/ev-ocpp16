package com.ev.ocpp16.domain.chargingManagement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargeStep;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryException;
import com.ev.ocpp16.domain.common.entity.BaseTimeEntity;
import com.ev.ocpp16.domain.site.entity.SiteRate;

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
import lombok.Builder;
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

    @Column(name = "action_datetime")
    private LocalDateTime actionDatetime;

    @Column(name = "meter_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal meterValue;

    @Column(name = "charging_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal chargingRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_history_id", nullable = false)
    private ChargeHistory chargeHistory;

    @Builder
    public ChargeHistoryDetail(
            ChargeHistory chargeHistory,
            SiteRate siteRate,
            BigDecimal meterValue,
            LocalDateTime actionDatetime,
            ChargeStep chargeStep) {
        this.chargeHistory = chargeHistory;
        this.chargingRate = calculateChargingPrice(siteRate, meterValue);
        this.meterValue = meterValue;
        this.actionDatetime = actionDatetime;
        this.chargeStep = chargeStep;
    }

    private BigDecimal calculateChargingPrice(SiteRate siteRate, BigDecimal meterValue)
            throws ChargeHistoryException {
        if (siteRate.getMemberDiscount().compareTo(BigDecimal.ZERO) < 0
                || siteRate.getMemberDiscount().compareTo(BigDecimal.valueOf(100)) > 0
                || siteRate.getNonMemberDiscount().compareTo(BigDecimal.ZERO) < 0
                || siteRate.getNonMemberDiscount().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ChargeHistoryException("할인율은 0~100 사이의 값이어야 합니다.");
        }

        boolean isMember = !"".equals(this.chargeHistory.getMember().getIdToken());
        BigDecimal discountRate = isMember ? siteRate.getMemberDiscount() : siteRate.getNonMemberDiscount();

        return siteRate.getRatePerWh()
                .multiply(meterValue)
                .multiply(BigDecimal.ONE.subtract(discountRate.divide(BigDecimal.valueOf(100))));
    }
}
