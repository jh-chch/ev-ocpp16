package com.ev.ocpp16.web.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ChargerErrorHistoryQueryDTO {

    @Data
    public static class Request {
        @NotBlank
        @Length(max = 30)
        private String siteName;

        @Length(max = 30)
        private String serialNumber;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startDateTime;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endDateTime;
    }

    @Data
    public static class Response {
        private String siteName;
        private String serialNumber;
        private int connectorId;
        private String status;
        private LocalDateTime errorDatetime;
        private LocalDateTime resolvedDateTime;
        private String errorCode;
        private String description;
    }
}
