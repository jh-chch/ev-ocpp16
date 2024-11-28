package com.ev.ocpp16.domain.chargepoint.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.ChgrConnUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.dto.ChgrInfoUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.entity.Charger;
import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerConnectorRepository;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerRepository;

import lombok.RequiredArgsConstructor;

/**
 * Chgr: Charger
 * Conn: Connector
 * St: Status
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChargerService {
    private final ChargerRepository chargerRepository;
    private final ChargerConnectorRepository chargerConnectorRepository;
    
    public boolean isChgrActiveTrue(Long chgrId) {
        return chargerRepository.findByIdAndIsActiveTrue(chgrId).isPresent();
    }

    public void updateAllChgrConnSt(ChgrConnSt chgrConnSt) {
        chargerRepository.updateAllChgrConnSt(chgrConnSt);
    }

    public boolean updateChgrConnSt(Long chgrId, ChgrConnSt chgrConnSt) {
        return chargerRepository.findByIdAndIsActiveTrue(chgrId)
                .map(charger -> {
                    charger.changeChgrConnSt(chgrConnSt);
                    return true;
                })
                .orElse(false);
    }

    // 충전기 정보 업데이트 성공: true, 실패: false
    public Optional<Charger> updateChgrInfo(ChgrInfoUpdateDTO dto) {
        return chargerRepository.findByIdAndIsActiveTrue(dto.getChgrId())
                .map(charger -> {
                    charger.changeChgrInfo(dto);
                    return Optional.of(charger);
                })
                .orElse(Optional.empty());
    }

    // 충전기 커넥터 상태 변경
    public Optional<ChgrConnUpdateDTO> updateChgrConn(ChgrConnUpdateDTO dto) {
        return chargerConnectorRepository.findByChargerIdAndConnectorId(dto.getChgrId(), dto.getConnectorId())
                .map(chgrConn -> {
                    chgrConn.changeChgrSt(dto.getChargePointStatus());
                    return Optional.of(dto);
                })
                .orElse(Optional.empty());
    }




}
