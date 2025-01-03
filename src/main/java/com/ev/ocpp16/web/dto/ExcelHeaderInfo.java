package com.ev.ocpp16.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExcelHeaderInfo {
    private String label;
    private String value;
    private int rowIndex; // 행 위치
    private int columnIndex; // 열 위치
    private int colspan; // 열 병합 수
}
