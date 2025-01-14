package com.ev.ocpp16.application;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.Roles;
import com.ev.ocpp16.domain.member.exception.MemberNotFoundException;
import com.ev.ocpp16.domain.member.service.MemberCommandService;
import com.ev.ocpp16.domain.member.service.MemberQueryService;
import com.ev.ocpp16.web.dto.MemberQueryDTO;
import com.ev.ocpp16.web.dto.MemberSaveDTO;
import com.ev.ocpp16.web.dto.MembersQueryDTO;
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
     * @return 회원 정보
     * @throws ApiException
     */
    public MemberQueryDTO.Response getMemberByIdToken(String idToken) {
        try {
            return new MemberQueryDTO.Response(memberQueryService.getMemberByIdToken(idToken));
        } catch (MemberNotFoundException e) {
            throw new ApiException(ApiExceptionStatus.NOT_FOUND_MEMBER);
        }
    }

    /**
     * 회원 목록 조회
     * 
     * @param siteName    충전소 이름
     * @param searchType  검색 타입
     * @param searchValue 검색 값
     * @return 회원 목록
     */
    public MembersQueryDTO.Response getMembers(MembersQueryDTO.Request request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize());
        return MembersQueryDTO.Response.of(
                memberQueryService.getMembers(
                        request.getSiteName(),
                        request.getSearchType(),
                        request.getSearchValue(),
                        pageable));
    }

    /**
     * 회원 등록
     * 
     * @param request
     * @return 회원 정보
     * @throws ApiException
     */
    @Transactional
    public MemberSaveDTO.Response saveMember(MemberSaveDTO.Request request) {
        try {
            Member member = request.toEntity(Roles.ROLE_USER);
            Member savedMember = memberCommandService.saveMember(member);
            return new MemberSaveDTO.Response(savedMember);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ApiExceptionStatus.DUPLICATE_VALUE, e.getMessage());
        }
    }

}
