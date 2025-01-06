package com.ev.ocpp16.domain.chargingManagement.repository.query;

import static com.ev.ocpp16.domain.chargingManagement.entity.QChargeHistory.chargeHistory;
import static com.ev.ocpp16.domain.chargingManagement.entity.QCharger.charger;
import static com.ev.ocpp16.domain.chargingManagement.entity.QChargerConnector.chargerConnector;
import static com.ev.ocpp16.domain.member.entity.QMember.member;
import static com.ev.ocpp16.domain.site.entity.QSite.site;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ev.ocpp16.domain.chargingManagement.entity.ChargeHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class HistoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    public HistoryQueryRepository(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<ChargeHistory> findChargeHistory(String idToken, LocalDateTime startDatetime,
            LocalDateTime endDatetime) {
        List<ChargeHistory> findChargeHistories = queryFactory
                .select(chargeHistory)
                .from(chargeHistory)
                .leftJoin(chargeHistory.member, member).fetchJoin()
                .leftJoin(chargeHistory.chargerConnector, chargerConnector).fetchJoin()
                .leftJoin(chargerConnector.charger, charger).fetchJoin()
                .leftJoin(charger.site, site).fetchJoin()
                .where(
                        idTokenEq(idToken),
                        dateCondition(startDatetime, endDatetime))
                .fetch();

        if (findChargeHistories.isEmpty()) {
            return Collections.emptyList();
        }

        return findChargeHistories;
    }

    private BooleanExpression idTokenEq(String idToken) {
        if (idToken == null) {
            return null;
        }
        return member.idToken.eq(idToken);
    }

    private BooleanExpression dateCondition(LocalDateTime startDatetime, LocalDateTime endDatetime) {
        // startDatetime과 endDatetime이 모두 null인 경우 조건 제외
        if (startDatetime == null && endDatetime == null) {
            return null;
        }

        // startDatetime이 null이면 시작일 조건 제외
        if (startDatetime == null) {
            return chargeHistory.endDatetime.loe(endDatetime);
        }

        // endDatetime이 null이면 종료일 조건 제외
        if (endDatetime == null) {
            return chargeHistory.startDatetime.goe(startDatetime);
        }

        // startDatetime과 endDatetime이 모두 있는 경우 시작일과 종료일 조건 추가
        return chargeHistory.startDatetime.goe(startDatetime)
                .and(chargeHistory.endDatetime.loe(endDatetime));
    }
}
