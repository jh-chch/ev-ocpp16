package com.ev.ocpp16.domain.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ev.ocpp16.domain.member.entity.enums.Roles;
import com.ev.ocpp16.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmailAndRoles(email, Roles.ROLE_ADMIN)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다: " + email));
    }
}
