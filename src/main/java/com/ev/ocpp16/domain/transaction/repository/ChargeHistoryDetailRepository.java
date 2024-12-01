package com.ev.ocpp16.domain.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.transaction.entity.ChargeHistoryDetail;

public interface ChargeHistoryDetailRepository extends JpaRepository<ChargeHistoryDetail, Long> {
    Optional<ChargeHistoryDetail> findFirstByChargeHistoryIdOrderByIdDesc(Integer chargeHistoryId);
}
