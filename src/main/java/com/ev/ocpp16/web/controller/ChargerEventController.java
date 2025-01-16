package com.ev.ocpp16.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ev.ocpp16.application.ChargerEventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chargers/events")
@RequiredArgsConstructor
public class ChargerEventController {

    private final ChargerEventService chargerEventService;

    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToChargerEvents(@RequestParam(name = "siteName") String siteName) {
        return chargerEventService.subscribe(siteName);
    }
}
