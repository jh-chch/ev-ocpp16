package com.ev.ocpp16.web.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExcelMakeDTO {
    private final String sheetName;
    private List<ExcelHeaderInfo> additionalHeaders;
    private final String[] headers;
    private final List<Object[]> rows;

    public String getSheetName() {
        return sheetName != null ? sheetName : "sheetName";
    }

    public List<ExcelHeaderInfo> getAdditionalHeaders() {
        return additionalHeaders != null ? additionalHeaders : List.of();
    }

    public String[] getHeaders() {
        return headers != null ? headers : new String[0];
    }

    public List<Object[]> getRows() {
        return rows != null ? rows : List.of();
    }
}
