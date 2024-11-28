package com.ev.ocpp16.domain.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.transaction.entity.ChargerError;

public interface ChargerErrorRepository extends JpaRepository<ChargerError, Integer> {

    Optional<ChargerError> findByErrorCode(String errorCode);

}
