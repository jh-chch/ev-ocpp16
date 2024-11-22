package com.ev.ocpp16.websocket.entity;

import com.ev.ocpp16.websocket.entity.enums.ChgrConnSt;

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

@Entity
@Table(name = "charger")
@Getter
public class Charger extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charger_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "serial_number", unique = true, length = 30)
    private String serialNumber;

    @Column(name = "model", length = 30)
    private String model;

    @Column(name = "vendor", length = 30)
    private String vendor;

    @Column(name = "firmware_version", length = 30)
    private String firmwareVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status", nullable = false)
    private ChgrConnSt chgrConnSt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;
}
