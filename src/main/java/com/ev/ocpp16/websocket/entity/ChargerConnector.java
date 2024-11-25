package com.ev.ocpp16.websocket.entity;

import com.ev.ocpp16.websocket.protocol.action.dto.types.ChargePointStatus;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Table(name = "charger_connector", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "charger_id", "connector_id" })
})
@Getter
public class ChargerConnector extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charger_connector_id")
    private Long id;

    @Column(name = "connector_id", nullable = false)
    private int connectorId;

    @Column(name = "type", length = 30)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_point_status", length = 30)
    private ChargePointStatus chargePointStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_id", nullable = false)
    private Charger charger;
}
