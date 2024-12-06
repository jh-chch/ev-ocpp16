package com.ev.ocpp16.domain.chargepoint.dto.api;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class ChgrsQueryDTO {

    @Data
    public static class Request {
        @NotBlank
        @Length(max = 30)
        private String siteName;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private List<ChgrQueryDTO.Response> chargers;

        public static Response of(List<ChgrQueryDTO.Response> chargers) {
            return new Response(chargers);
        }
    }
}
