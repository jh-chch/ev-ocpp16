package com.ev.ocpp16.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ev.ocpp16.domain.chargingManagement.event.ChargerStatusChangedEvent;
import com.ev.ocpp16.domain.chargingManagement.service.ChargerQueryService;
import com.ev.ocpp16.web.dto.ChargersQueryDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargerEventService {
    private final ChargerQueryService chargerQueryService;
    private final Map<String, Set<SseEmitter>> siteEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String siteName) {
        SseEmitter emitter = new SseEmitter(15 * 60 * 1000L);

        // 초기 데이터 전송
        try {
            ChargersQueryDTO.Response chargers = ChargersQueryDTO.Response
                    .of(chargerQueryService.getChargers(siteName));
            emitter.send(SseEmitter.event()
                .data(chargers)
                .id("init"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            return emitter;
        }

        // 에미터 등록
        siteEmitters.computeIfAbsent(siteName, k -> ConcurrentHashMap.newKeySet())
                .add(emitter);

        // 완료, 타임아웃, 에러 시 정리
        emitter.onCompletion(() -> removeEmitter(siteName, emitter));
        emitter.onTimeout(() -> removeEmitter(siteName, emitter));
        emitter.onError(e -> removeEmitter(siteName, emitter));

        return emitter;
    }

    private void removeEmitter(String siteName, SseEmitter emitter) {
        Set<SseEmitter> emitters = siteEmitters.get(siteName);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                siteEmitters.remove(siteName);
            }
        }
    }

    @EventListener
    public void handleChargerStatusChanged(ChargerStatusChangedEvent event) {
        Set<SseEmitter> emitters = siteEmitters.get(event.getSiteName());
        if (emitters != null) {
            ChargersQueryDTO.Response updatedChargers = ChargersQueryDTO.Response
                    .of(chargerQueryService.getChargers(event.getSiteName()));

            List<SseEmitter> deadEmitters = new ArrayList<>();
            
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                        .data(updatedChargers)
                        .id(UUID.randomUUID().toString()));
                } catch (IOException e) {
                    deadEmitters.add(emitter);
                }
            });
            
            deadEmitters.forEach(emitter -> removeEmitter(event.getSiteName(), emitter));
        }
    }

}
