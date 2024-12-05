package com.ev.ocpp16.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ev.ocpp16.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdToken(String idToken);

    Optional<Member> findByEmail(String email);

    boolean existsByIdToken(String idToken);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCarNumber(String carNumber);

}
