package com.ev.ocpp16.domain.chargingManagement.repository.query;

import static com.ev.ocpp16.domain.chargingManagement.entity.QCharger.charger;
import static com.ev.ocpp16.domain.chargingManagement.entity.QChargerConnector.chargerConnector;
import static com.ev.ocpp16.domain.site.entity.QSite.site;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ev.ocpp16.domain.chargingManagement.entity.Charger;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class ChargerQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ChargerQueryRepository(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<Charger> findChargers(String siteName) {
        List<Charger> fetch = queryFactory
                .select(charger)
                .from(charger)
                .leftJoin(charger.chargerConnectors, chargerConnector).fetchJoin()
                .join(charger.site, site).fetchJoin()
                .where(siteNmEq(siteName))
                .fetch();

        return fetch;
    }

    public Optional<Charger> findCharger(String serialNumber, String siteName) {
        return Optional.ofNullable(
                queryFactory
                        .select(charger)
                        .from(charger)
                        .join(charger.site, site).fetchJoin()
                        .where(
                                siteNmEq(siteName),
                                chgrSerialEq(serialNumber))
                        .fetchOne());
    }

    public BooleanExpression siteNmEq(String siteNm) {
        if (siteNm == null) {
            return null;
        }
        return charger.site.name.eq(siteNm);
    }

    public BooleanExpression chgrSerialEq(String serialNumber) {
        if (serialNumber == null) {
            return null;
        }
        return charger.serialNumber.eq(serialNumber);
    }
}
