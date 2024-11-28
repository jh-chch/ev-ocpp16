package com.ev.ocpp16.domain.chargepoint.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;

public interface ChargerConnectorRepository extends JpaRepository<ChargerConnector, Long> {

    Optional<ChargerConnector> findByChargerIdAndConnectorId(Long chgrId, Integer connId);

}
