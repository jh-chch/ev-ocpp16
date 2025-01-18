package com.ev.ocpp16.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    
    @Value("${spring.profiles.active}")
    private String profile;
    
    @GetMapping("/profile")
    public String getProfile() {
        return profile;
    }
    
    @GetMapping("/health")
    public String health() {
        return profile;
    }
}
