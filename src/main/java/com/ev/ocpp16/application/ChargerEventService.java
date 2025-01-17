package com.ev.ocpp16.application;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.user.SimpUserRegistry;

import com.ev.ocpp16.domain.chargingManagement.event.ChargerStatusChangedEvent;
import com.ev.ocpp16.domain.chargingManagement.service.ChargerQueryService;
import com.ev.ocpp16.web.dto.ChargersQueryDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargerEventService {
    private final ChargerQueryService chargerQueryService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @EventListener
    public void handleChargerStatusChanged(ChargerStatusChangedEvent event) {
        ChargersQueryDTO.Response updatedChargers = ChargersQueryDTO.Response
                .of(chargerQueryService.getChargers(event.getSiteName()));

        // 현재 구독자 정보 로깅
        String destination = "/topic/chargers/" + event.getSiteName();
        simpUserRegistry.getUsers().forEach(user -> {
            user.getSessions().forEach(session -> {
                Set<String> subscriptions = session.getSubscriptions().stream()
                    .map(subscription -> subscription.getDestination())
                    .collect(Collectors.toSet());
                
                log.info("User Session ID: {}, Subscriptions: {}", 
                    session.getId(), 
                    subscriptions);
            });
        });

        // 상태 변경 시 해당 사이트의 모든 구독자에게 메시지 전송
        messagingTemplate.convertAndSend(destination, updatedChargers);
    }
}
