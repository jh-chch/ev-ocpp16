package com.ev.ocpp16.websocket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.websocket.entity.Charger;

public interface ChargerRepository extends JpaRepository<Charger, Long> {
    Optional<Charger> findByIdAndIsActiveTrue(Long chgrId);
}
