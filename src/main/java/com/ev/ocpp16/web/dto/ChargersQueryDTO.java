package com.ev.ocpp16.web.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Length;

import com.ev.ocpp16.domain.chargingManagement.entity.Charger;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ChargersQueryDTO {

    @Data
    public static class Request {
        @NotBlank
        @Length(max = 30)
        private String siteName;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private List<ChargerQueryDTO.Response> chargers;

        public static Response of(List<Charger> chargers) {
            if (chargers == null) {
                return null;
            }
            return new Response(
                    chargers.stream()
                            .map(ChargerQueryDTO.Response::of)
                            .collect(Collectors.toList()));
        }
    }
}
