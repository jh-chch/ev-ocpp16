package com.ev.ocpp16.application;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ev.ocpp16.global.utils.DateTimeUtil;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO.Response.ChargeHistoryDTO;
import com.ev.ocpp16.web.dto.ExcelHeaderInfo;
import com.ev.ocpp16.web.dto.ExcelMakeDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChargeHistoryExcelGenerator {

	private final ExcelService excelService;

	private static final String SHEET_NAME = "충전이력";
	private static final int DEFAULT_COLSPAN = 2;

	public byte[] createSummaryExcelData(ChargeHistoryQueryDTO.Request request,
			ChargeHistoryQueryDTO.Response chargeHistory) throws IOException {
		return excelService.createExcelResponse(
				ExcelMakeDTO.builder()
						.sheetName(SHEET_NAME)
						.headers(createSummaryHeaders())
						.additionalHeaders(createSummaryAdditionalHeaders(request, chargeHistory))
						.rows(createSummaryRows(chargeHistory))
						.build());
	}

	public byte[] createDetailedExcelData(String idToken, ChargeHistoryQueryDTO.Request request,
			ChargeHistoryQueryDTO.Response chargeHistory) throws IOException {
		return excelService.createExcelResponse(
				ExcelMakeDTO.builder()
						.sheetName(SHEET_NAME)
						.headers(createDetailedHeaders())
						.additionalHeaders(createDetailedAdditionalHeaders(idToken, request, chargeHistory))
						.rows(createDetailedRows(chargeHistory))
						.build());
	}

	private String[] createDetailedHeaders() {
		return new String[] {
				"충전소", "충전기", "시작시간", "종료시간", "충전량(Wh)", "충전요금(원)"
		};
	}

	private String[] createSummaryHeaders() {
		return new String[] {
				"충전기", "시작시간", "종료시간", "idToken", "이름", "휴대폰", "주소",
				"차량번호", "충전량(Wh)", "충전요금(원)"
		};
	}

	private List<Object[]> createDetailedRows(ChargeHistoryQueryDTO.Response chargeHistory) {
		if (chargeHistory.getChargeHistories().isEmpty()) {
			return Arrays.asList();
		}

		return chargeHistory.getChargeHistories().stream()
				.map(history -> new Object[] {
						history.getSiteName(),
						history.getChargerName(),
						history.getStartDatetime(),
						history.getEndDatetime(),
						history.getTotalMeterValue(),
						history.getTotalPrice()
				})
				.collect(Collectors.toList());
	}

	private List<Object[]> createSummaryRows(ChargeHistoryQueryDTO.Response chargeHistory) {
		if (chargeHistory.getChargeHistories().isEmpty()) {
			return Arrays.asList();
		}

		return chargeHistory.getChargeHistories().stream()
				.map(history -> new Object[] {
						history.getChargerName(),
						history.getStartDatetime(),
						history.getEndDatetime(),
						history.getIdToken(),
						history.getUsername(),
						history.getPhoneNumber(),
						history.getAddress(),
						history.getCarNumber(),
						history.getTotalMeterValue(),
						history.getTotalPrice()
				})
				.collect(Collectors.toList());
	}

	private List<ExcelHeaderInfo> createDetailedAdditionalHeaders(String idToken,
			ChargeHistoryQueryDTO.Request request,
			ChargeHistoryQueryDTO.Response chargeHistory) {

		if (request.getStartDatetime() == null) {
			return Arrays.asList();
		}

		if (chargeHistory == null || chargeHistory.getChargeHistories().isEmpty()) {
			return Arrays.asList();
		}

		ChargeHistoryDTO firstHistory = chargeHistory.getChargeHistories().get(0);

		return Arrays.asList(
				createHeaderInfo("생성일시", DateTimeUtil.currentKoreanLocalDateTime().toString(), 0),
				createHeaderInfo("조회기간", formatDateRange(request), 1),
				createHeaderInfo("idToken", idToken, 2),
				createHeaderInfo("이름", firstHistory.getUsername(), 3),
				createHeaderInfo("휴대폰", firstHistory.getPhoneNumber(), 4),
				createHeaderInfo("주소", firstHistory.getAddress().toString(), 5),
				createHeaderInfo("차량번호", firstHistory.getCarNumber(), 6));
	}

	private List<ExcelHeaderInfo> createSummaryAdditionalHeaders(
			ChargeHistoryQueryDTO.Request request,
			ChargeHistoryQueryDTO.Response chargeHistory) {

		if (request == null) {
			return Arrays.asList();
		}

		if (chargeHistory == null || chargeHistory.getChargeHistories().isEmpty()) {
			return Arrays.asList();
		}

		return Arrays.asList(
				createHeaderInfo("생성일시", DateTimeUtil.currentKoreanLocalDateTime().toString(), 0),
				createHeaderInfo("조회기간", formatDateRange(request), 1),
				createHeaderInfo("충전소", chargeHistory.getChargeHistories().get(0).getSiteName(), 2));
	}

	private ExcelHeaderInfo createHeaderInfo(String label, String value, int rowIndex) {
		return ExcelHeaderInfo.builder()
				.label(label)
				.value(value)
				.rowIndex(rowIndex)
				.columnIndex(0)
				.colspan(DEFAULT_COLSPAN)
				.build();
	}

	private String formatDateRange(ChargeHistoryQueryDTO.Request request) {
		return new StringBuilder()
				.append(request.getStartDatetime() != null ? request.getStartDatetime() : "")
				.append(" ~ ")
				.append(request.getEndDatetime() != null ? request.getEndDatetime() : "")
				.toString();
	}
}
