package com.ev.ocpp16.web.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.AccountStatus;
import com.ev.ocpp16.domain.member.entity.enums.Address;
import com.ev.ocpp16.domain.member.entity.enums.Roles;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class MembersQueryDTO {

    @Data
    public static class Request {
        @NotBlank
        @Length(max = 30)
        private String siteName;

        @Pattern(regexp = "email|username|carNumber")
        private String searchType;

        @Length(max = 30)
        private String searchValue;

        @Min(0)
        private int page;

        @Min(1)
        @Max(100)
        private int size;
    }

    @Getter
    @Builder
    public static class Response {
        private List<MemberDTO> members;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private int currentPage;
        private int size;
        private boolean hasPrevious;
        private boolean isFirst;
        private boolean isLast;

        public static Response of(Page<Member> page) {
            return Response.builder()
                    .members(page.getContent().stream()
                            .map(MemberDTO::from)
                            .collect(Collectors.toList()))
                    .totalPages(page.getTotalPages())
                    .totalElements(page.getTotalElements())
                    .hasNext(page.hasNext())
                    .currentPage(page.getNumber())
                    .size(page.getSize())
                    .hasPrevious(page.hasPrevious())
                    .isFirst(page.isFirst())
                    .isLast(page.isLast())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class MemberDTO {
        private String email;
        private String username;
        private String idToken;
        private String phoneNumber;
        private String carNumber;
        private Address address;
        private Roles role;
        private AccountStatus accountStatus;

        public static MemberDTO from(Member member) {
            return MemberDTO.builder()
                    .email(member.getEmail())
                    .username(member.getUsername())
                    .idToken(member.getIdToken())
                    .phoneNumber(member.getPhoneNumber())
                    .carNumber(member.getCarNumber())
                    .address(member.getAddress())
                    .role(member.getRoles())
                    .accountStatus(member.getAccountStatus())
                    .build();
        }
    }
}
