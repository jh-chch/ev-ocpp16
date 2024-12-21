package com.ev.ocpp16.domain.chargingManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;

public interface ChargeHistoryRepository extends JpaRepository<ChargeHistory, Integer> {

}
