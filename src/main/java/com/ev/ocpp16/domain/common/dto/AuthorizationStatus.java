package com.ev.ocpp16.domain.common.dto;

public enum AuthorizationStatus {
    Accepted, // 식별자 충전이 허용
    Blocked, // 식별자 차단
    Expired, // 식별자 만료
    Invalid, // 식별자 알 수 없음
    ConcurrentTx; // 식별자 이미 충전중 (StartTransaction.req에만 해당)
}