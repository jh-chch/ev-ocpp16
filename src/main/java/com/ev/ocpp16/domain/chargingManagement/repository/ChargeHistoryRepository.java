package com.ev.ocpp16.domain.chargingManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;

public interface ChargeHistoryRepository extends JpaRepository<ChargeHistory, Integer> {
    @EntityGraph(attributePaths = { "member", "chargerConnector", "chargerConnector.charger" })
    Optional<ChargeHistory> findById(Integer transactionId);
}
