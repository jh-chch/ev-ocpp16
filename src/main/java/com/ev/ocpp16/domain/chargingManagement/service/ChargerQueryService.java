package com.ev.ocpp16.domain.chargingManagement.service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectorStatus;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerConnectorNotFoundException;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerException;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerNotFoundException;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargerConnectorRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargerRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.query.ChargerQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChargerQueryService {

    private final ChargerRepository chargerRepository;
    private final ChargerConnectorRepository chargerConnectorRepository;
    private final ChargerQueryRepository chargerQueryRepository;

    /**
     * 충전기 검증
     * 
     * @param chargerIdentifier 충전기 식별자
     * @return
     * @throws ChargerNotFoundException
     * @throws ChargerException
     */
    public Charger validateChargerForCharging(String chargerIdentifier)
            throws ChargerNotFoundException, ChargerException {
        Charger findCharger = chargerRepository.findBySerialNumber(chargerIdentifier)
                .orElseThrow(() -> new ChargerNotFoundException("Charger not found: " + chargerIdentifier));

        if (findCharger.isActive()) {
            return findCharger;
        }

        throw new ChargerException("충전기 상태가 충전 가능하지 않습니다.");
    }

    /**
     * 충전기 커넥터 조회
     * 
     * @param charger
     * @param connectorId
     * @return
     * @throws ChargerConnectorNotFoundException
     */
    public ChargerConnector getChargerConnector(Charger charger, Integer connectorId)
            throws ChargerConnectorNotFoundException {
        return chargerConnectorRepository.findByChargerIdAndConnectorId(charger.getId(), connectorId)
                .orElseThrow(() -> new ChargerConnectorNotFoundException(
                        "ChargerConnector not found: " + charger.getId() + ", " + connectorId));
    }

    /**
     * 충전 시작 가능한 상태인지 확인
     * 
     * @param chargerConnector
     * @return
     */
    public boolean canStartCharging(ChargerConnector chargerConnector) {
        return EnumSet.of(
                ConnectorStatus.AVAILABLE,
                ConnectorStatus.RESERVED,
                ConnectorStatus.PREPARING)
                .contains(chargerConnector.getConnectorStatus());
    }

    /**
     * 충전기 목록 조회
     * 
     * @param siteName
     * @return
     * @throws ChargerException
     */
    public List<Charger> getChargers(String siteName) throws ChargerException {
        if (siteName == null) {
            throw new ChargerException("사이트 이름이 필요합니다.");
        }

        if (siteName.length() > 30) {
            throw new ChargerException("사이트 이름은 30자 이하로 입력해야 합니다.");
        }

        return chargerQueryRepository.findChargers(siteName);
    }

    /**
     * 충전기 조회
     * 
     * @param serialNumber
     * @param siteName
     * @return
     * @throws ChargerException
     */
    public Optional<Charger> getCharger(String serialNumber, String siteName) throws ChargerException {
        if (serialNumber == null || siteName == null) {
            throw new ChargerException("시리얼 번호와 사이트 이름이 필요합니다.");
        }

        if (serialNumber.length() > 30) {
            throw new ChargerException("시리얼 번호는 30자 이하로 입력해야 합니다.");
        }

        if (siteName.length() > 30) {
            throw new ChargerException("사이트 이름은 30자 이하로 입력해야 합니다.");
        }

        return chargerQueryRepository.findCharger(serialNumber, siteName);
    }

}
