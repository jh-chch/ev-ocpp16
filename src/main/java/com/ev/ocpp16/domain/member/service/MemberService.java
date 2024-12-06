package com.ev.ocpp16.domain.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.common.exception.ApiException;
import com.ev.ocpp16.domain.common.exception.ApiExceptionStatus;
import com.ev.ocpp16.domain.member.dto.api.MemberQueryDTO;
import com.ev.ocpp16.domain.member.dto.api.MemberSaveDTO;
import com.ev.ocpp16.domain.member.dto.fromChargePoint.ChargingMemberDTO;
import com.ev.ocpp16.domain.member.entity.enums.Roles;
import com.ev.ocpp16.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<ChargingMemberDTO> getMemberForCharging(String idToken) {
        return memberRepository.findByIdToken(idToken)
                .map(ChargingMemberDTO::new);
    }

    // 회원 등록
    public MemberSaveDTO.Response saveMember(MemberSaveDTO.Request request) {
        if (memberRepository.existsByIdToken(request.getIdToken())) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_ID_TOKEN);
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_EMAIL);
        }

        if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_PHONE_NUMBER);
        }

        if (memberRepository.existsByCarNumber(request.getCarNumber())) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_CAR_NUMBER);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(request.toEntity(Roles.ROLE_USER));

        return new MemberSaveDTO.Response(request.getIdToken(), request.getUsername(), request.getEmail());
    }

    // 회원 조회
    public MemberQueryDTO.Response getMemberByIdToken(String idToken) {
        return memberRepository.findByIdToken(idToken)
                .map(MemberQueryDTO.Response::new)
                .orElseThrow(() -> new ApiException(ApiExceptionStatus.NOT_FOUND_MEMBER));
    }
}
