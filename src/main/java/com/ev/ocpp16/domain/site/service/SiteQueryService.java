package com.ev.ocpp16.domain.site.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.site.entity.Site;
import com.ev.ocpp16.domain.site.repository.SiteRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SiteQueryService {

    private final SiteRepository siteRepository;

    /**
     * 사이트 목록 조회
     * 
     * @return 사이트 목록
     */
    public List<String> getSites() {
        return siteRepository.findAll().stream().map(Site::getName).toList();
    }
}
