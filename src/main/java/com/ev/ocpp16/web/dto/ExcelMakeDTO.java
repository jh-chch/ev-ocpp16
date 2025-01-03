package com.ev.ocpp16.web.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExcelMakeDTO {
    private final String fileName;
    private final String sheetName;
    private List<ExcelHeaderInfo> additionalHeaders;
    private final String[] headers;
    private final List<Object[]> rows;
}
