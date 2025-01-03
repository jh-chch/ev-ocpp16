package com.ev.ocpp16.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ev.ocpp16.application.ChargeInfoService;
import com.ev.ocpp16.application.ExcelService;
import com.ev.ocpp16.application.MembershipService;
import com.ev.ocpp16.global.utils.DateTimeUtil;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;
import com.ev.ocpp16.web.dto.ChargersQueryDTO;
import com.ev.ocpp16.web.dto.ExcelHeaderInfo;
import com.ev.ocpp16.web.dto.ExcelMakeDTO;
import com.ev.ocpp16.web.dto.MembersQueryDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ChargeInfoService chargeInfoService;
    private final MembershipService membershipService;
    private final ExcelService excelService;

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

    @GetMapping("/members/{idToken}/charge-history/excel")
    public ResponseEntity<byte[]> downloadChargeHistory(
            @PathVariable("idToken") @Length(max = 20) String idToken,
            @Validated @ModelAttribute ChargeHistoryQueryDTO.Request request) throws IOException {
        // 충전 이력 조회
        ChargeHistoryQueryDTO.Response chargeHistory = chargeInfoService.getChargeHistory(idToken, request);

        // Excel 생성 및 다운로드
        return excelService.createExcelResponse(
                ExcelMakeDTO.builder()
                        .fileName(createFileName(idToken, request))
                        .sheetName("충전이력")
                        .headers(createHeaders())
                        .additionalHeaders(createAdditionalHeaders(idToken, request, chargeHistory))
                        .rows(createRows(chargeHistory))
                        .build());
    }

    private String createFileName(String idToken, ChargeHistoryQueryDTO.Request request) {
        StringBuilder fileName = new StringBuilder("충전이력_")
                .append(idToken)
                .append("_")
                .append(request.getStartDatetime() == null
                        ? DateTimeUtil.currentKoreanLocalDateTime().toString()
                        : request.getStartDatetime().toString())
                .append("_")
                .append(request.getEndDatetime() == null
                        ? DateTimeUtil.currentKoreanLocalDateTime().toString()
                        : request.getEndDatetime().toString())
                .append(".xlsx");
        return fileName.toString();
    }

    private String[] createHeaders() {
        return new String[] {
                "충전소", "충전기", "시작시간", "종료시간", "충전량(Wh)", "충전요금(원)"
        };
    }

    private List<Object[]> createRows(ChargeHistoryQueryDTO.Response chargeHistory) {
        List<Object[]> rows = chargeHistory.getChargeHistories().stream()
                .map(history -> new Object[] {
                        history.getSiteName(),
                        history.getChargerName(),
                        history.getStartDatetime(),
                        history.getEndDatetime(),
                        history.getTotalMeterValue(),
                        history.getTotalPrice()
                })
                .collect(Collectors.toList());
        return rows;
    }

    private List<ExcelHeaderInfo> createAdditionalHeaders(String idToken, ChargeHistoryQueryDTO.Request request,
            ChargeHistoryQueryDTO.Response chargeHistory) {
        List<ExcelHeaderInfo> additionalHeaders = Arrays.asList(
                createHeaderInfo("생성일시", DateTimeUtil.currentKoreanLocalDateTime().toString(), 0, 0, 2),
                createHeaderInfo("조회기간", request.getStartDatetime() + " ~ " + request.getEndDatetime(), 1, 0, 2),
                createHeaderInfo("idToken", idToken, 2, 0, 2),
                createHeaderInfo("이름", chargeHistory.getChargeHistories().get(0).getUsername(), 3, 0, 2),
                createHeaderInfo("휴대폰", chargeHistory.getChargeHistories().get(0).getPhoneNumber(), 4, 0, 2),
                createHeaderInfo("주소", chargeHistory.getChargeHistories().get(0).getAddress().toString(), 5, 0, 2),
                createHeaderInfo("차량번호", chargeHistory.getChargeHistories().get(0).getCarNumber(), 6, 0, 2));
        return additionalHeaders;
    }

    private ExcelHeaderInfo createHeaderInfo(String label, String value, int rowIndex, int columnIndex, int colspan) {
        return ExcelHeaderInfo.builder()
                .label(label)
                .value(value)
                .rowIndex(rowIndex)
                .columnIndex(columnIndex)
                .colspan(colspan)
                .build();
    }
}
