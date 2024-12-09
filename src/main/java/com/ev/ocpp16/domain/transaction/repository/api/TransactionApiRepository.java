package com.ev.ocpp16.domain.transaction.repository.api;

import static com.ev.ocpp16.domain.chargepoint.entity.QCharger.charger;
import static com.ev.ocpp16.domain.chargepoint.entity.QChargerConnector.chargerConnector;
import static com.ev.ocpp16.domain.chargepoint.entity.QSite.site;
import static com.ev.ocpp16.domain.member.entity.QMember.member;
import static com.ev.ocpp16.domain.transaction.entity.QChargeHistory.chargeHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ev.ocpp16.domain.transaction.dto.api.ChgrHstQueryDTO;
import com.ev.ocpp16.domain.transaction.dto.api.QChgrHstQueryDTO_Response;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class TransactionApiRepository {
    private final JPAQueryFactory queryFactory;

    public TransactionApiRepository(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<List<ChgrHstQueryDTO.Response>> findChgrHsts(ChgrHstQueryDTO.Request request) {
        List<ChgrHstQueryDTO.Response> findChgrHsts = queryFactory
                .select(
                        new QChgrHstQueryDTO_Response(
                                chargeHistory.startDatetime, chargeHistory.endDatetime,
                                chargeHistory.totalMeterValue, member.carNumber))
                .from(chargeHistory)
                .leftJoin(chargeHistory.member, member)
                .leftJoin(chargeHistory.chargerConnector, chargerConnector)
                .leftJoin(chargerConnector.charger, charger)
                .leftJoin(charger.site, site)
                .where(
                        siteNmEq(request.getSiteName())
                                .and(serialNumberEq(request.getSerialNumber()))
                                .and(idTokenEq(request.getIdToken()))
                                .and(dateCondition(request.getStartDatetime(), request.getEndDatetime())))
                .fetch();

        if (findChgrHsts.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(findChgrHsts);
    }

    private BooleanExpression siteNmEq(String siteName) {
        return site.name.eq(siteName);
    }

    private BooleanExpression serialNumberEq(String serialNumber) {
        if (serialNumber == null) {
            return null;
        }
        return charger.serialNumber.eq(serialNumber);
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
