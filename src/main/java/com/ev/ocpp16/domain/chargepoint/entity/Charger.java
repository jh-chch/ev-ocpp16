package com.ev.ocpp16.domain.chargepoint.entity;

import com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint.ChgrInfoUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.ev.ocpp16.domain.common.entity.BaseTimeEntity;

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

    @Column(name = "model", length = 30)
    private String model;

    @Column(name = "serial_number", unique = true, length = 30)
    private String serialNumber;

    @Column(name = "vendor", length = 30)
    private String vendor;

    @Column(name = "firmware_version", length = 30)
    private String firmwareVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status")
    private ChgrConnSt chgrConnSt;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    public void changeChgrConnSt(ChgrConnSt chgrConnSt) {
        this.chgrConnSt = chgrConnSt;
    }

    public void changeChgrInfo(ChgrInfoUpdateDTO chgrInfoUpdateDTO) {
        this.model = chgrInfoUpdateDTO.getModel();
        this.vendor = chgrInfoUpdateDTO.getVendor();

        if (chgrInfoUpdateDTO.getSerialNumber() != null) {
            this.serialNumber = chgrInfoUpdateDTO.getSerialNumber();
        }

        if (chgrInfoUpdateDTO.getFirmwareVersion() != null) {
            this.firmwareVersion = chgrInfoUpdateDTO.getFirmwareVersion();
        }
    }

}
