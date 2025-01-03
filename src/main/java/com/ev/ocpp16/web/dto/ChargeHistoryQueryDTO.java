package com.ev.ocpp16.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;
import com.ev.ocpp16.domain.member.entity.enums.Address;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

public class ChargeHistoryQueryDTO {

    @Data
    @NoArgsConstructor
    public static class Request {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startDatetime;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endDatetime;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private List<ChargeHistoryDTO> chargeHistories;

        public static Response of(List<ChargeHistory> chargeHistories) {
            return new Response(
                    chargeHistories.stream()
                            .map(ch -> new ChargeHistoryDTO(
                                    ch.getChargerConnector().getCharger().getSite().getName(),
                                    ch.getStartDatetime(),
                                    ch.getEndDatetime(),
                                    ch.getTotalPrice(),
                                    ch.getTotalMeterValue(),
                                    ch.getMember().getUsername(),
                                    ch.getMember().getPhoneNumber(),
                                    ch.getMember().getAddress(),
                                    ch.getMember().getCarNumber(),
                                    ch.getChargerConnector().getCharger().getName()))
                            .collect(Collectors.toList()));
        }

        @Data
        @AllArgsConstructor
        public static class ChargeHistoryDTO {
            private String siteName;
            private LocalDateTime startDatetime;
            private LocalDateTime endDatetime;
            private BigDecimal totalPrice;
            private BigDecimal totalMeterValue;
            private String username;
            private String phoneNumber;
            private Address address;
            private String carNumber;
            private String chargerName;
        }
    }

}
