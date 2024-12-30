package com.ev.ocpp16.domain.site.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ev.ocpp16.domain.site.entity.SiteRate;

public interface SiteRateRepository extends JpaRepository<SiteRate, Integer> {
    Optional<SiteRate> findBySiteIdAndHour(@Param("siteId") Integer siteId, @Param("hour") int hour);
}
