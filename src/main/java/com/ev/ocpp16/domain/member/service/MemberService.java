package com.ev.ocpp16.domain.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.common.exception.ApiException;
import com.ev.ocpp16.domain.common.exception.ApiExceptionStatus;
import com.ev.ocpp16.domain.member.dto.api.MemberRegisterDTO;
import com.ev.ocpp16.domain.member.dto.fromChargePoint.MemberDTO;
import com.ev.ocpp16.domain.member.entity.enums.Roles;
import com.ev.ocpp16.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<MemberDTO> getMemberByIdToken(String idToken) {
        return memberRepository.findByIdToken(idToken)
                .map(MemberDTO::new);
    }

    // 회원 등록
    public MemberRegisterDTO.Response registerMember(MemberRegisterDTO.Request request) {
        if (memberRepository.existsByIdToken(request.getIdToken())) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_ID_TOKEN);
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_EMAIL);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(request.toEntity(Roles.ROLE_USER));
        
        return new MemberRegisterDTO.Response(request.getIdToken(), request.getUsername(), request.getEmail());
    }

}
