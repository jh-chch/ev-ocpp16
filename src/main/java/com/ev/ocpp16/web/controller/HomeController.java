package com.ev.ocpp16.web.controller;

import java.io.IOException;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ev.ocpp16.application.ChargeHistoryExcelGenerator;
import com.ev.ocpp16.application.ChargeInfoService;
import com.ev.ocpp16.application.MembershipService;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;
import com.ev.ocpp16.web.dto.ChargersQueryDTO;
import com.ev.ocpp16.web.dto.MembersQueryDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ChargeInfoService chargeInfoService;
    private final MembershipService membershipService;
    private final ChargeHistoryExcelGenerator excelGenerator;

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

    @GetMapping("/members/charge-history/excel")
    public ResponseEntity<byte[]> downloadSummaryChargeHistory(
            @Validated @ModelAttribute ChargeHistoryQueryDTO.Request request) throws IOException {
        // 충전 이력 조회
        ChargeHistoryQueryDTO.Response chargeHistory = chargeInfoService.getChargeHistory(null, request);

        // Excel 생성 및 다운로드
        return ResponseEntity.ok(excelGenerator.createSummaryExcelData(request, chargeHistory));
    }

    @GetMapping("/members/{idToken}/charge-history/excel")
    public ResponseEntity<byte[]> downloadDetailedChargeHistory(
            @PathVariable("idToken") @Length(max = 20) String idToken,
            @Validated @ModelAttribute ChargeHistoryQueryDTO.Request request) throws IOException {
        // 충전 이력 조회
        ChargeHistoryQueryDTO.Response chargeHistory = chargeInfoService.getChargeHistory(idToken, request);

        // Excel 생성 및 다운로드
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(excelGenerator.createDetailedExcelData(idToken, request, chargeHistory));
    }
}
