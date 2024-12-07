package com.ev.ocpp16.domain.chargepoint.repository.api;

import static com.ev.ocpp16.domain.chargepoint.entity.QCharger.charger;
import static com.ev.ocpp16.domain.chargepoint.entity.QSite.site;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrQueryDTO;
import com.ev.ocpp16.domain.chargepoint.dto.api.ChgrsQueryDTO;
import com.ev.ocpp16.domain.chargepoint.dto.api.QChgrQueryDTO_Response;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class ChargerApiRepository {

    private final JPAQueryFactory queryFactory;

    public ChargerApiRepository(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<ChgrsQueryDTO.Response> findChgrsBySite(ChgrsQueryDTO.Request request) {
        List<ChgrQueryDTO.Response> findChgrs = queryFactory
                .select(new QChgrQueryDTO_Response(
                        charger.name, charger.model, charger.serialNumber, charger.vendor,
                        charger.firmwareVersion, charger.chgrConnSt, site.name))
                .from(charger)
                .join(charger.site, site)
                .where(siteNmEq(request.getSiteName()))
                .fetch();

        return Optional.of(ChgrsQueryDTO.Response.of(findChgrs));
    }

    public Optional<ChgrQueryDTO.Response> findChgrBySite(String serialNumber, ChgrQueryDTO.Request request) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QChgrQueryDTO_Response(
                                charger.name, charger.model, charger.serialNumber, charger.vendor,
                                charger.firmwareVersion, charger.chgrConnSt, site.name))
                        .from(charger)
                        .join(charger.site, site)
                        .where(siteNmEq(request.getSiteName()).and(chgrSerialEq(serialNumber)))
                        .fetchOne());
    }

    public BooleanExpression siteNmEq(String siteNm) {
        return charger.site.name.eq(siteNm);
    }

    public BooleanExpression chgrSerialEq(String serialNumber) {
        return charger.serialNumber.eq(serialNumber);
    }
}
