package com.ev.ocpp16.websocket.dto.fromChargePoint.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.ToString;

/**
 * @param chargeBoxSerialNumber   충전 상자 내부의 충전소의 일련번호
 *                                향후 버전에서는 제거될 예정
 * @param chargePointModel        충전소의 모델을 식별하는 값
 * @param chargePointSerialNumber 충전소의 일련번호를 식별하는 값
 * @param chargePointVendor       충전소의 제조업체를 식별하는 값
 * @param firmwareVersion         충전소의 펌웨어 버전
 * @param iccid                   모뎀의 SIM 카드 ICCID
 * @param imsi                    모뎀의 SIM 카드 IMSI
 * @param meterSerialNumber       충전소의 주 전력계 일련번호
 * @param meterType               충전소의 주 전력계 타입
 */
@Getter
@ToString
public class BootNotificationRequest {
    private String chargeBoxSerialNumber;
    private final String chargePointModel; // 1..1
    private String chargePointSerialNumber;
    private final String chargePointVendor; // 1..1
    private String firmwareVersion;
    private String iccid;
    private String imsi;
    private String meterSerialNumber;
    private String meterType;

    @JsonCreator
    public BootNotificationRequest(String chargePointModel, String chargePointVendor) {
        this.chargePointModel = chargePointModel;
        this.chargePointVendor = chargePointVendor;
    }
}