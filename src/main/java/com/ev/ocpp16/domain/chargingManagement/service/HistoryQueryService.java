package com.ev.ocpp16.domain.chargingManagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;
import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistoryDetail;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargeHistoryNotFoundException;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargeHistoryDetailRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.ChargeHistoryRepository;
import com.ev.ocpp16.domain.chargingManagement.repository.query.HistoryQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryQueryService {

    private final ChargeHistoryRepository chargeHistoryRepository;
    private final ChargeHistoryDetailRepository chargeHistoryDetailRepository;
    private final HistoryQueryRepository historyQueryRepository;

    /**
     * 충전 이력 조회
     * 
     * @param transactionId 충전 트랜잭션 ID
     * @return
     * @throws ChargeHistoryNotFoundException
     */
    public ChargeHistory getChargeHistory(Integer transactionId) throws ChargeHistoryNotFoundException {
        return chargeHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new ChargeHistoryNotFoundException("ChargeHistory not found"));
    }

    /**
     * 충전 이력 상세 조회
     * 
     * @param transactionId 충전 트랜잭션 ID
     * @return
     * @throws ChargeHistoryNotFoundException
     */
    public ChargeHistoryDetail getFirstChargeHistoryDetail(Integer transactionId) throws ChargeHistoryNotFoundException {
        return chargeHistoryDetailRepository
                .findFirstByChargeHistoryIdOrderById(transactionId)
                .orElseThrow(() -> new ChargeHistoryNotFoundException("ChargeHistoryDetail not found"));
    }

    /**
     * 회원 충전 이력 조회
     * 
     * @param request
     * @return
     */
    public List<ChargeHistory> getMemberChargeHistory(
            String idToken,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime) {
        // 시작 날짜는 있는데 끝 날짜가 없으면 시작 날짜 기준 7일치
        if (startDatetime != null && endDatetime == null) {
            endDatetime = startDatetime.plusDays(7);
        }

        // 끝 날짜는 있는데 시작 날짜가 없으면 끝 날짜 기준 7일치
        else if (startDatetime == null && endDatetime != null) {
            startDatetime = endDatetime.minusDays(7);
        }

        // 둘다 없으면 오늘 날짜 기준 7일치
        else if (startDatetime == null && endDatetime == null) {
            startDatetime = LocalDateTime.now().minusDays(7);
            endDatetime = LocalDateTime.now();
        }

        return historyQueryRepository.findChargeHistory(idToken, startDatetime, endDatetime);
    }
}
