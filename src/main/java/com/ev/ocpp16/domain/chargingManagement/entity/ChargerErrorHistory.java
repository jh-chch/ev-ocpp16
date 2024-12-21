package com.ev.ocpp16.domain.chargingManagement.entity;

import java.time.LocalDateTime;

import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargerErrorCode;

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
@Table(name = "charger_error_history")
@Getter
@NoArgsConstructor
public class ChargerErrorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charger_error_history_id")
    private Integer id;

    @Column(name = "error_code", nullable = false, length = 30)
    private ChargerErrorCode errorCode;

    @Column(name = "info", columnDefinition = "TEXT")
    private String info;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('active','resolved') default 'active'")
    private Status status;

    @Column(name = "error_datetime", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime errorDatetime;

    @Column(name = "resolved_datetime")
    private LocalDateTime resolvedDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_connector_id", nullable = false)
    private ChargerConnector chargerConnector;

    @Builder
    public ChargerErrorHistory(ChargerErrorCode errorCode, String info, ChargerConnector chargerConnector) {
        this.errorCode = errorCode;
        this.info = info;
        this.chargerConnector = chargerConnector;
        this.status = Status.active;
        this.errorDatetime = LocalDateTime.now();
    }

    public enum Status {
        active,
        resolved
    }

}
