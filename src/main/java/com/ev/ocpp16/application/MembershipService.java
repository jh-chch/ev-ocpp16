package com.ev.ocpp16.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.member.dto.MemberQueryDTO;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.Roles;
import com.ev.ocpp16.domain.member.exception.DuplicateMemberException;
import com.ev.ocpp16.domain.member.exception.InvalidMemberException;
import com.ev.ocpp16.domain.member.exception.MemberException;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.member.service.MemberCommandService;
import com.ev.ocpp16.domain.member.service.MemberQueryService;
import com.ev.ocpp16.web.dto.MemberSaveDTO;
import com.ev.ocpp16.web.exception.ApiException;
import com.ev.ocpp16.web.exception.ApiExceptionStatus;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MembershipService {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    /**
     * 회원 조회
     * 
     * @param idToken
     * @return
     */
    public MemberQueryDTO.Response getMember(String idToken) {
        try {
            return new MemberQueryDTO.Response(memberQueryService.getMember(idToken));
        } catch (MemberNotFoundException e) {
            throw new ApiException(ApiExceptionStatus.NOT_FOUND_MEMBER);
        }
    }

    /**
     * 회원 등록
     * 
     * @param request
     * @return
     */
    @Transactional
    public MemberSaveDTO.Response saveMember(MemberSaveDTO.Request request) {
        try {
            Member member = request.toEntity(Roles.ROLE_USER);
            Member savedMember = memberCommandService.saveMember(member);
            return new MemberSaveDTO.Response(savedMember);
        } catch (InvalidMemberException e) {
            throw new ApiException(ApiExceptionStatus.INVALID_VALUE, e.getMessage());
        } catch (DuplicateMemberException e) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_VALUE, e.getMessage());
        } catch (MemberException e) {
            throw new ApiException(ApiExceptionStatus.INVALID_VALUE, e.getMessage());
        }
    }

}
