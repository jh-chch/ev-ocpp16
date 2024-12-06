package com.ev.ocpp16.domain.chargepoint.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrQueryDTO;
import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrsQueryDTO;
import com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint.ChgrConnStUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.dto.fromChargePoint.ChgrInfoUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.entity.Charger;
import com.ev.ocpp16.domain.chargepoint.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.ev.ocpp16.domain.chargepoint.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.chargepoint.exception.ChargerNotFoundException;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerApiRepository;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerConnectorRepository;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerRepository;
import com.ev.ocpp16.domain.chargepoint.repository.SiteRepository;
import com.ev.ocpp16.domain.common.exception.ApiException;
import com.ev.ocpp16.domain.common.exception.ApiExceptionStatus;

import lombok.RequiredArgsConstructor;

/**
 * Chgr: Charger
 * Conn: Connector
 * St: Status
 * Nm: Name
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChargerService {
    private final ChargerRepository chargerRepository;
    private final SiteRepository siteRepository;
    private final ChargerConnectorRepository chargerConnectorRepository;
    private final ChargerApiRepository chargerApiRepository;

    public boolean isChgrActiveTrue(Long chgrId) {
        return chargerRepository.findByIdAndIsActiveTrue(chgrId).isPresent();
    }

    public void updateAllChgrConnSt(ChgrConnSt chgrConnSt) {
        chargerRepository.updateAllChgrConnSt(chgrConnSt);
    }

    public void updateChgrConnSt(Long chgrId, ChgrConnSt chgrConnSt) {
        chargerRepository.findByIdAndIsActiveTrue(chgrId)
                .ifPresent(charger -> charger.changeChgrConnSt(chgrConnSt));
    }

    // 충전기 정보 업데이트
    public void updateChgrInfo(ChgrInfoUpdateDTO dto) throws ChargerNotFoundException {
        Charger findChgr = chargerRepository.findByIdAndIsActiveTrue(dto.getChgrId())
                .orElseThrow(() -> new ChargerNotFoundException(dto.getChgrId()));

        findChgr.changeChgrInfo(dto);
    }

    // 충전기 커넥터 상태 업데이트
    public void updateChgrConnSt(ChgrConnStUpdateDTO dto) throws ChargerConnectorNotFoundException {
        ChargerConnector findChgrConn = chargerConnectorRepository
                .findByChargerIdAndConnectorId(dto.getChgrId(), dto.getConnectorId())
                .orElseThrow(() -> new ChargerConnectorNotFoundException(dto.getChgrId(), dto.getConnectorId()));

        findChgrConn.changeChgrSt(dto.getChargePointStatus());
    }

    // 사이트의 모든 충전기 조회
    public ChgrsQueryDTO.Response getChgrsBySite(ChgrsQueryDTO.Request request) {
        return chargerApiRepository.findChgrsBySite(request)
                .map(chgr -> {
                    if (chgr.getChargers().isEmpty()) {
                        throw new ApiException(ApiExceptionStatus.INVALID_SITE_NAME);
                    }
                    return chgr;
                }).get();
    }

    // 사이트의 특정 충전기 조회
    public ChgrQueryDTO.Response getChgrBySite(String serialNumber, ChgrQueryDTO.Request request) {
        // 사이트 존재 여부 확인
        if (!siteRepository.existsByName(request.getSiteName())) {
            throw new ApiException(ApiExceptionStatus.INVALID_SITE_NAME);
        }

        return chargerApiRepository.findChgrBySite(serialNumber, request)
                .orElseThrow(() -> new ApiException(ApiExceptionStatus.INVALID_SERIAL_NUMBER));
    }
}
