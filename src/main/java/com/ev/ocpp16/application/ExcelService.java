package com.ev.ocpp16.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ev.ocpp16.web.dto.ExcelHeaderInfo;
import com.ev.ocpp16.web.dto.ExcelMakeDTO;

@Service
public class ExcelService {

    /**
     * @param dto Excel 생성 요청 DTO
     * @return ResponseEntity<byte[]> Excel 파일 바이트 배열
     * @throws IOException 파일 입출력 예외
     */
    public ResponseEntity<byte[]> createExcelResponse(ExcelMakeDTO dto) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            createSheet(workbook, dto);
            byte[] excelBytes = convertToBytes(workbook);
            return ResponseEntity.ok()
                    .headers(createHeaders(dto.getFileName()))
                    .body(excelBytes);
        }
    }

    private void createSheet(Workbook workbook, ExcelMakeDTO dto) {
        Sheet sheet = workbook.createSheet(dto.getSheetName());
        int startRowNum = 0;

        // 추가 헤더 정보 처리
        if (dto.getAdditionalHeaders() != null && !dto.getAdditionalHeaders().isEmpty()) {
            createAdditionalHeaders(sheet, dto.getAdditionalHeaders());
            // 마지막 추가 헤더 행 이후에 빈 행 하나 추가
            startRowNum = dto.getAdditionalHeaders().stream()
                    .mapToInt(ExcelHeaderInfo::getRowIndex)
                    .max()
                    .orElse(-1) + 2;
        }

        // 컬럼 헤더 생성
        if (dto.getHeaders() != null) {
            Row headerRow = sheet.createRow(startRowNum);
            createHeaderRow(headerRow, dto.getHeaders());
            startRowNum++;
        }

        // 데이터 입력
        createDataRows(sheet, dto.getRows(), startRowNum);

        // 컬럼 너비 자동 조정
        autoSizeColumns(sheet, dto.getHeaders() != null ? dto.getHeaders().length : dto.getRows().get(0).length);
    }

    private void createAdditionalHeaders(Sheet sheet, List<ExcelHeaderInfo> additionalHeaders) {
        CellStyle labelStyle = createLabelCellStyle(sheet.getWorkbook());
        CellStyle valueStyle = createValueCellStyle(sheet.getWorkbook());

        for (ExcelHeaderInfo headerInfo : additionalHeaders) {
            Row row = sheet.getRow(headerInfo.getRowIndex());
            if (row == null) {
                row = sheet.createRow(headerInfo.getRowIndex());
            }

            // 레이블 셀 생성
            Cell labelCell = row.createCell(headerInfo.getColumnIndex());
            labelCell.setCellValue(headerInfo.getLabel());
            labelCell.setCellStyle(labelStyle);

            // 값 셀 생성
            int valueColumnIndex = headerInfo.getColumnIndex() + 1;
            Cell valueCell = row.createCell(valueColumnIndex);
            valueCell.setCellValue(headerInfo.getValue());
            valueCell.setCellStyle(valueStyle);

            // 셀 병합 처리 - 수정된 부분
            if (headerInfo.getColspan() > 1) {
                CellRangeAddress mergeRange = new CellRangeAddress(
                        headerInfo.getRowIndex(),
                        headerInfo.getRowIndex(),
                        valueColumnIndex,
                        valueColumnIndex + headerInfo.getColspan() - 2
                );

                // 병합할 범위가 유효한지 확인
                if (mergeRange.getNumberOfCells() >= 2) {
                    sheet.addMergedRegion(mergeRange);
                }
            }
        }
    }

    private CellStyle createLabelCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createValueCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private void createHeaderRow(Row headerRow, String[] headers) {
        CellStyle headerStyle = createHeaderCellStyle(headerRow.getSheet().getWorkbook());
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createDataRows(Sheet sheet, List<Object[]> rows, int startRowNum) {
        CellStyle dataStyle = createDataCellStyle(sheet.getWorkbook());
        for (Object[] rowData : rows) {
            Row row = sheet.createRow(startRowNum++);
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                setCellValue(cell, rowData[i]);
                cell.setCellStyle(dataStyle);
            }
        }
    }

    private CellStyle createDataCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            // 자동 조정된 너비에 약간의 여유 추가
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, (int) (currentWidth * 1.2));
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private byte[] convertToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private HttpHeaders createHeaders(String fileName) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", buildContentDisposition(fileName));
        return headers;
    }

    private String buildContentDisposition(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = encodeFileName(fileName);
        StringBuilder contentDisposition = new StringBuilder("attachment; filename=\"");
        contentDisposition.append(new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                .append("\"; filename*=UTF-8''")
                .append(encodedFileName);
        return contentDisposition.toString();
    }

    private String encodeFileName(String fileName) throws UnsupportedEncodingException {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replace("+", "%20");
    }
}
