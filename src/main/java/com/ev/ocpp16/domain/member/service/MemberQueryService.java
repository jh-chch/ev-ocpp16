package com.ev.ocpp16.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.exception.MemberException;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    /**
     * 회원 조회
     * 
     * @param idToken
     * @return
     * @throws MemberNotFoundException
     */
    public Member getMember(String idToken) {
        return memberRepository.findByIdToken(idToken)
                .orElseThrow(() -> new MemberNotFoundException(idToken));
    }

    /**
     * 충전 가능한 회원인지 확인
     * 
     * @param idToken
     * @return
     * @throws MemberNotFoundException
     * @throws MemberException
     */
    public Member validateMemberForCharging(String idToken) throws MemberNotFoundException, MemberException {
        Member findMember = memberRepository.findByIdToken(idToken)
                .orElseThrow(() -> new MemberNotFoundException(idToken));

        if (findMember.isActive()) {
            return findMember;
        }

        throw new MemberException("회원 상태가 충전 가능하지 않습니다.");
    }

}
