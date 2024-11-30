package com.ev.ocpp16.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.transaction.entity.ChargeHistoryDetail;

public interface ChargeHistoryDetailRepository extends JpaRepository<ChargeHistoryDetail, Long> {

}
