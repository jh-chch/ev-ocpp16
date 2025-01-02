package com.ev.ocpp16.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ev.ocpp16.application.ChargeInfoService;
import com.ev.ocpp16.application.MembershipService;
import com.ev.ocpp16.web.dto.ChargersQueryDTO;
import com.ev.ocpp16.web.dto.MembersQueryDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ChargeInfoService chargeInfoService;
    private final MembershipService membershipService;

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

    @ResponseBody
    @GetMapping("/chargers")
    public ResponseEntity<ChargersQueryDTO.Response> getChargers(
            @Validated @ModelAttribute ChargersQueryDTO.Request request) {
        return ResponseEntity.ok(chargeInfoService.getChargers(request));
    }

    @ResponseBody
    @GetMapping("/members")
    public ResponseEntity<MembersQueryDTO.Response> getMembers(
            @Validated @ModelAttribute MembersQueryDTO.Request request) {
        return ResponseEntity.ok(membershipService.getMembers(request));
    }
}
