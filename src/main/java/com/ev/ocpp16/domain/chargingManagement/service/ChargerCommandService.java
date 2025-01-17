package com.ev.ocpp16.domain.chargingManagement.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargingManagement.dto.ChargerInfoUpdateRequestDTO;
import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectionStatus;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectorStatus;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerException;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChargerCommandService {

    private final ChargerRepository chargerRepository;

    /**
     * 충전기 연결 상태 업데이트
     * 
     * @param chgrConnSt 충전기 연결 상태
     */
    public void updateAllChargerConnectionStatus(ConnectionStatus chgrConnSt) {
        if (chgrConnSt == null) {
            throw new ChargerException("충전기 연결 상태는 필수입니다.");
        }
        chargerRepository.updateAllChargerConnectionStatus(chgrConnSt);
    }

    /**
     * 충전기 연결 상태 업데이트
     * 
     * @param charger          충전기
     * @param connectionStatus 충전기 연결 상태
     * 
     * @throws ChargerException
     */
    public void updateChargerConnectionStatus(Charger charger, ConnectionStatus connectionStatus)
            throws ChargerException {
        if (charger == null) {
            throw new ChargerException("충전기는 필수입니다.");
        }

        if (connectionStatus == null) {
            throw new ChargerException("충전기 연결 상태는 필수입니다.");
        }

        charger.changeConnectionStatus(connectionStatus);
    }

    /**
     * 충전기 커넥터 상태 업데이트
     * 
     * @param chargerConnector 충전기 커넥터
     * @param connectorStatus  충전기 커넥터 상태
     */
    public void updateChargerConnectorStatus(ChargerConnector chargerConnector, ConnectorStatus connectorStatus) {
        if (chargerConnector == null) {
            throw new ChargerException("충전기 커넥터는 필수입니다.");
        }

        if (connectorStatus == null) {
            throw new ChargerException("충전기 커넥터 상태는 필수입니다.");
        }

        chargerConnector.changeConnectorStatus(connectorStatus);
    }

    /**
     * 충전기 정보 업데이트
     * 
     * @param charger 충전기
     * @param dto     충전기 정보 업데이트 요청 DTO
     */
    public boolean updateChargerInfo(Charger charger, ChargerInfoUpdateRequestDTO dto) {
        if (charger == null && dto == null) {
            throw new ChargerException("충전기와 충전기 정보 업데이트 요청 데이터는 필수입니다.");
        }

        if (dto.getChargePointModel() == null || dto.getChargePointVendor() == null) {
            throw new ChargerException("모델과 공급자는 필수입니다.");
        }

        // 길이 제한 검사
        if (dto.getChargePointModel().length() > 30) {
            throw new ChargerException("모델 이름은 30자를 초과할 수 없습니다.");
        }
        if (dto.getChargePointVendor().length() > 30) {
            throw new ChargerException("공급자 이름은 30자를 초과할 수 없습니다.");
        }
        if (dto.getChargePointSerialNumber() != null && dto.getChargePointSerialNumber().length() > 30) {
            throw new ChargerException("시리얼 번호는 30자를 초과할 수 없습니다.");
        }
        if (dto.getFirmwareVersion() != null && dto.getFirmwareVersion().length() > 30) {
            throw new ChargerException("펌웨어 버전은 30자를 초과할 수 없습니다.");
        }

        return charger.changeChargerInfo(
                dto.getChargePointModel(),
                dto.getChargePointVendor(),
                dto.getChargePointSerialNumber(),
                dto.getFirmwareVersion());
    }

}
