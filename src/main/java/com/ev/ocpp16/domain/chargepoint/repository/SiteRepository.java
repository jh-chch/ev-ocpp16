package com.ev.ocpp16.domain.chargepoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.chargepoint.entity.Site;

public interface SiteRepository extends JpaRepository<Site, Long> {
    Boolean existsByName(String name);
}
