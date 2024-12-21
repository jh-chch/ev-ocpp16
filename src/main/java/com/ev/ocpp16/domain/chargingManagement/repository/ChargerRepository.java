package com.ev.ocpp16.domain.chargingManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectionStatus;

public interface ChargerRepository extends JpaRepository<Charger, Long> {
    @Modifying(clearAutomatically = true)
    @Query("update Charger c set c.connectionStatus = :connectionStatus")
    void updateAllChargerConnectionStatus(ConnectionStatus connectionStatus);

    Optional<Charger> findBySerialNumber(@Param("serialNumber") String chargerIdentifier);
}
