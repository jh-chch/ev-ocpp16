package com.ev.ocpp16.domain.site.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.site.entity.SiteRate;
import com.ev.ocpp16.domain.site.exception.SiteRateException;
import com.ev.ocpp16.domain.site.repository.SiteRateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiteRateQueryService {

    private final SiteRateRepository siteRateRepository;

    /**
     * 사이트 단가 조회
     * 
     * @param siteId
     * @param dayOfWeek
     * @param hour
     * @return
     * @throws SiteRateException
     */
    public SiteRate getSiteRate(Integer siteId, int hour) throws SiteRateException {
        return siteRateRepository.findBySiteIdAndHour(siteId, hour)
                .orElseThrow(() -> new SiteRateException("사이트 단가를 찾을 수 없습니다."));
    }
}
