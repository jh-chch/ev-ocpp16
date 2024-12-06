package com.ev.ocpp16.domain.chargepoint.dto.api;

import org.hibernate.validator.constraints.Length;

import com.ev.ocpp16.domain.chargepoint.entity.enums.ChgrConnSt;
import com.querydsl.core.annotations.QueryProjection;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ChgrQueryDTO {

    @Data
    public static class Request {
        @NotBlank
        @Length(max = 30)
        private String siteName;
    }

    @Data
    public static class Response {
        private String name;
        private String model;
        private String serialNumber;
        private String vendor;
        private String firmwareVersion;
        private ChgrConnSt chargerConnectorStatus;
        private String siteName;

        @QueryProjection
        public Response(String name, String model, String serialNumber, String vendor, String firmwareVersion,
                ChgrConnSt chargerConnectorStatus, String siteName) {
            this.name = name;
            this.model = model;
            this.serialNumber = serialNumber;
            this.vendor = vendor;
            this.firmwareVersion = firmwareVersion;
            this.chargerConnectorStatus = chargerConnectorStatus;
            this.siteName = siteName;
        }
    }
}
