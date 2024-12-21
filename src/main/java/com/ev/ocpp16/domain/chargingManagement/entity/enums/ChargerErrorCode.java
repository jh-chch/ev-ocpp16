package com.ev.ocpp16.domain.chargingManagement.entity.enums;

import lombok.Getter;

@Getter
public enum ChargerErrorCode {
    CONNECTOR_LOCK_FAILURE("커넥터 잠금 또는 잠금 해제 실패"),
    EV_COMMUNICATION_ERROR("차량과의 통신 실패"),
    GROUND_FAILURE("접지 차단 장치가 작동"),
    HIGH_TEMPERATURE("내부 온도가 너무 높음"),
    INTERNAL_ERROR("내부 하드웨어 또는 소프트웨어 구성요소 오류"),
    LOCAL_LIST_CONFLICT("인증 정보 충돌"),
    NO_ERROR(""),
    OTHER_ERROR("기타 오류"),
    OVER_CURRENT_FAILURE("과전류 보호 장치 작동"),
    OVER_VOLTAGE("전압 허용 수준 초과"),
    POWER_METER_FAILURE("전력계 읽기 실패"),
    POWER_SWITCH_FAILURE("전원 스위치 제어 실패"),
    READER_FAILURE("idTag 리더기 오류"),
    RESET_FAILURE("재설정 수행 불가"),
    UNDER_VOLTAGE("전압이 허용 수준 아래로 떨어짐"),
    WEAK_SIGNAL("무선 통신 장치 약한 신호");

    private String info;

    ChargerErrorCode(String info) {
        this.info = info;
    }
}
