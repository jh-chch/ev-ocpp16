package com.ev.ocpp16.websocket.dto.fromChargePoint.common;

import com.ev.ocpp16.domain.chargingManagement.entity.enums.ChargerErrorCode;

public enum ChargePointErrorCode {
    ConnectorLockFailure, // 커넥터 잠금 또는 잠금 해제 실패
    EVCommunicationError, // 차량과의 통신 실패
    GroundFailure, // 접지 차단 장치가 작동
    HighTemperature, // 내부 온도가 너무 높음
    InternalError, // 내부 하드웨어 또는 소프트웨어 구성요소 오류
    LocalListConflict, // 인증 정보 충돌
    NoError,
    OtherError,
    OverCurrentFailure, // 과전류 보호 장치 작동
    OverVoltage, // 전압 허용 수준 초과
    PowerMeterFailure, // 전력계 읽기 실패
    PowerSwitchFailure, // 전원 스위치 제어 실패
    ReaderFailure, // idTag 리더기 오류
    ResetFailure, // 재설정 수행 불가.
    UnderVoltage, // 전압이 허용 수준 아래로 떨어짐
    WeakSignal; // 무선 통신 장치 약한 신호

    public ChargerErrorCode toChargerErrorCode() {
        switch (this) {
            case ConnectorLockFailure:
                return ChargerErrorCode.CONNECTOR_LOCK_FAILURE;
            case EVCommunicationError:
                return ChargerErrorCode.EV_COMMUNICATION_ERROR;
            case GroundFailure:
                return ChargerErrorCode.GROUND_FAILURE;
            case HighTemperature:
                return ChargerErrorCode.HIGH_TEMPERATURE;
            case InternalError:
                return ChargerErrorCode.INTERNAL_ERROR;
            case LocalListConflict:
                return ChargerErrorCode.LOCAL_LIST_CONFLICT;
            case NoError:
                return ChargerErrorCode.NO_ERROR;
            case OtherError:
                return ChargerErrorCode.OTHER_ERROR;
            case OverCurrentFailure:
                return ChargerErrorCode.OVER_CURRENT_FAILURE;
            case OverVoltage:
                return ChargerErrorCode.OVER_VOLTAGE;
            case PowerMeterFailure:
                return ChargerErrorCode.POWER_METER_FAILURE;
            case PowerSwitchFailure:
                return ChargerErrorCode.POWER_SWITCH_FAILURE;
            case ReaderFailure:
                return ChargerErrorCode.READER_FAILURE;
            case ResetFailure:
                return ChargerErrorCode.RESET_FAILURE;
            case UnderVoltage:
                return ChargerErrorCode.UNDER_VOLTAGE;
            case WeakSignal:
                return ChargerErrorCode.WEAK_SIGNAL;
            default:
                throw new IllegalArgumentException("Unknown ChargePointErrorCode: " + this);
        }
    }
}
