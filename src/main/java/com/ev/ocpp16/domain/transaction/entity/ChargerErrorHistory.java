package com.ev.ocpp16.domain.transaction.entity;

import java.time.LocalDateTime;

import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;

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
@Table(name = "charger_error_history")
@Getter
@NoArgsConstructor
public class ChargerErrorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charger_error_history_id")
    private Integer id;

    @Column(name = "info", columnDefinition = "TEXT")
    private String info;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('active','resolved') default 'active'")
    private Status status;

    @Column(name = "error_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime errorDate;

    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_connector_id", nullable = false)
    private ChargerConnector chargerConnector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_error_id", nullable = false)
    private ChargerError chargerError;

    public ChargerErrorHistory(String info, ChargerConnector chargerConnector, ChargerError chargerError) {
        this.info = info;
        this.chargerConnector = chargerConnector;
        this.chargerError = chargerError;
        this.status = Status.active;
        this.errorDate = LocalDateTime.now();
    }

    public enum Status {
        active,
        resolved
    }

}
