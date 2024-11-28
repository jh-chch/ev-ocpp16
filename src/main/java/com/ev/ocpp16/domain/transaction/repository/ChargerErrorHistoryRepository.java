package com.ev.ocpp16.domain.transaction.repository;    

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.transaction.entity.ChargerErrorHistory;

public interface ChargerErrorHistoryRepository extends JpaRepository<ChargerErrorHistory, Long> {

}
