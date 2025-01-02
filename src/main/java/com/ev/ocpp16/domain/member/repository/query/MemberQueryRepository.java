package com.ev.ocpp16.domain.member.repository.query;

import static com.ev.ocpp16.domain.member.entity.QMember.member;
import static com.ev.ocpp16.domain.site.entity.QSite.site;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ev.ocpp16.domain.member.entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MemberQueryRepository(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<Member> findByMembers(String siteName, String searchType, String searchValue, Pageable pageable) {
        List<Member> content = queryFactory
                .select(member)
                .from(member)
                .join(member.site, site).fetchJoin()
                .where(siteNameEq(siteName), searchCondition(searchType, searchValue))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(member.count())
                .from(member)
                .where(siteNameEq(siteName), searchCondition(searchType, searchValue))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression siteNameEq(String siteName) {
        return siteName != null ? member.site.name.eq(siteName) : null;
    }

    private BooleanExpression searchCondition(String searchType, String searchValue) {
        if (searchType == null || searchValue == null) {
            return null;
        }
        if ("email".equals(searchType)) {
            return member.email.startsWith(searchValue);
        } else if ("username".equals(searchType)) {
            return member.username.startsWith(searchValue);
        } else if ("carNumber".equals(searchType)) {
            return member.carNumber.startsWith(searchValue);
        }
        return null;
    }

}
