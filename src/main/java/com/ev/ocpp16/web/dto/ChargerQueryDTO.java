package com.ev.ocpp16.web.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Length;

import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargerConnector;
import com.ev.ocpp16.domain.chargingManagement.entity.enums.ConnectionStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ChargerQueryDTO {

    @Data
    public static class Request {
        @NotBlank
        @Length(max = 30)
        private String siteName;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private String name;
        private String model;
        private String serialNumber;
        private String vendor;
        private String firmwareVersion;
        private ConnectionStatus connectionStatus;
        private String siteName;
        private List<ChargerConnectorDTO> chargerConnectors;

        public static Response of(Charger charger) {
            if (charger == null) {
                return null;
            }
            return new Response(
                    charger.getName(),
                    charger.getModel(),
                    charger.getSerialNumber(),
                    charger.getVendor(),
                    charger.getFirmwareVersion(),
                    charger.getConnectionStatus(),
                    charger.getSite().getName(),
                    charger.getChargerConnectors().stream()
                            .map(ChargerConnectorDTO::of)
                            .collect(Collectors.toList()));
        }

        public static Response empty() {
            return new Response(null, null, null, null, null, null, null, null);
        }
    }

    @Data
    @AllArgsConstructor
    public static class ChargerConnectorDTO {
        private int connectorId;
        private String connectorStatus;

        public static ChargerConnectorDTO of(ChargerConnector chargerConnector) {
            return new ChargerConnectorDTO(
                    chargerConnector.getConnectorId(),
                    chargerConnector.getConnectorStatus().name());
        }
    }
}
