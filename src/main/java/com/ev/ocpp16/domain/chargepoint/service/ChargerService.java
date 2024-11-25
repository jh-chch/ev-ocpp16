package com.ev.ocpp16.domain.chargepoint.service;

import org.springframework.stereotype.Service;

import com.ev.ocpp16.websocket.repository.ChargerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargerService {
    private final ChargerRepository chargerRepository;

    public boolean isChgrActiveTrue(Long chgrId) {
        return chargerRepository.findByIdAndIsActiveTrue(chgrId).isPresent();
    }
}
