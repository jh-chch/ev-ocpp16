package com.ev.ocpp16.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ev.ocpp16.application.ChargeInfoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ChargeInfoService chargeInfoService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model) {
        List<String> sites = chargeInfoService.getSites();
        model.addAttribute("siteNames", sites);
        return "home";
    }
}
