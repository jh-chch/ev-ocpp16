package com.ev.ocpp16.domain.chargepoint.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ev.ocpp16.domain.chargepoint.entity.Charger;
import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;

public interface ChargerRepository extends JpaRepository<Charger, Long> {
    Optional<Charger> findByIdAndIsActiveTrue(Long chgrId);

    @Modifying(clearAutomatically = true)
    @Query("update Charger c set c.chgrConnSt = :chgrConnSt")
    void updateAllChgrConnSt(ChgrConnSt chgrConnSt);

}
