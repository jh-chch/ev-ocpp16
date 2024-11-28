package com.ev.ocpp16.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> getMemberByIdToken(String idToken) {
        return memberRepository.findByIdToken(idToken);
    }
}
