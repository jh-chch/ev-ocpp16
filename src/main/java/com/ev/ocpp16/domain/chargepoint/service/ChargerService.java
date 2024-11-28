package com.ev.ocpp16.domain.chargepoint.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargepoint.dto.ChgrInfoUpdateDTO;
import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.ev.ocpp16.domain.chargepoint.repository.ChargerRepository;

import lombok.RequiredArgsConstructor;

/**
 * Chgr: Charger
 * Conn: Connection
 * St: Status
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChargerService {
    private final ChargerRepository chargerRepository;

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

    public boolean updateChgrInfo(ChgrInfoUpdateDTO chgrInfoUpdateDTO) {
        return chargerRepository.findByIdAndIsActiveTrue(chgrInfoUpdateDTO.getChgrId())
                .map(charger -> {
                    charger.changeChgrInfo(chgrInfoUpdateDTO.getModel(), chgrInfoUpdateDTO.getSerialNumber(),
                            chgrInfoUpdateDTO.getVendor(), chgrInfoUpdateDTO.getFirmwareVersion());
                    return true;
                })
                .orElse(false);
    }
}
