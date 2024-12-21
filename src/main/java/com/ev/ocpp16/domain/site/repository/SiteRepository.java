package com.ev.ocpp16.domain.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.site.entity.Site;

public interface SiteRepository extends JpaRepository<Site, Long> {
    Boolean existsByName(String name);
}
