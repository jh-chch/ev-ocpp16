package com.ev.ocpp16.domain.transaction.dto.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.querydsl.core.annotations.QueryProjection;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ChgrHstQueryDTO {

    @Data
    public static class Request {
        @NotBlank
        @Length(max = 30)
        private String siteName;

        @Length(max = 30)
        private String serialNumber;

        @Length(max = 36)
        private String idToken;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startDatetime;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endDatetime;
    }

    @Data
    public static class Response {
        private LocalDateTime startDatetime;
        private LocalDateTime endDatetime;
        private BigDecimal totalMeterValue;
        private String carNumber;

        @QueryProjection
        public Response(LocalDateTime startDatetime, LocalDateTime endDatetime, BigDecimal totalMeterValue,
                String carNumber) {
            this.startDatetime = startDatetime;
            this.endDatetime = endDatetime;
            this.totalMeterValue = totalMeterValue;
            this.carNumber = carNumber;
        }
    }
}
