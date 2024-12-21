package com.ev.ocpp16.domain.chargingManagement.repository;    

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.chargingManagement.entity.ChargerErrorHistory;

public interface ChargerErrorHistoryRepository extends JpaRepository<ChargerErrorHistory, Long> {

}
