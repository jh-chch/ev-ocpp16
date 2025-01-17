package com.ev.ocpp16.domain.chargingManagement.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectionStatus;
import com.ev.ocpp16.domain.common.entity.BaseTimeEntity;
import com.ev.ocpp16.domain.site.entity.Site;

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
import jakarta.persistence.OneToMany;
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

    @Column(name = "model", length = 30)
    private String model;

    @Column(name = "serial_number", unique = true, nullable = false, length = 30)
    private String serialNumber;

    @Column(name = "vendor", length = 30)
    private String vendor;

    @Column(name = "firmware_version", length = 30)
    private String firmwareVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status")
    private ConnectionStatus connectionStatus;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    @OneToMany(mappedBy = "charger")
    private List<ChargerConnector> chargerConnectors = new ArrayList<>();

    public void changeConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public boolean changeChargerInfo(
            String model,
            String vendor,
            String serialNumber,
            String firmwareVersion) {
        
        boolean isChanged = false;
        
        if (!Objects.equals(this.model, model)) {
            this.model = model;
            isChanged = true;
        }
        if (!Objects.equals(this.vendor, vendor)) {
            this.vendor = vendor;
            isChanged = true;
        }
        if (!Objects.equals(this.serialNumber, serialNumber)) {
            this.serialNumber = serialNumber;
            isChanged = true;
        }
        if (!Objects.equals(this.firmwareVersion, firmwareVersion)) {
            this.firmwareVersion = firmwareVersion;
            isChanged = true;
        }
        
        return isChanged;
    }

}
