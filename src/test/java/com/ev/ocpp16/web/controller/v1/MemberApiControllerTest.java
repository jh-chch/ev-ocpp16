package com.ev.ocpp16.web.controller.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedQueryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.ev.ocpp16.application.ChargeInfoService;
import com.ev.ocpp16.application.MembershipService;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.AccountStatus;
import com.ev.ocpp16.domain.member.entity.enums.Address;
import com.ev.ocpp16.domain.member.entity.enums.Roles;
import com.ev.ocpp16.web.dto.ChargeHistoryQueryDTO;
import com.ev.ocpp16.web.dto.MemberQueryDTO;
import com.ev.ocpp16.web.dto.MembersQueryDTO;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
public class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private MembershipService membershipService;

    @MockBean
    private ChargeInfoService chargeInfoService;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    private HeaderDescriptor commonHeaders() {
        return headerWithName("Authorization")
                .description("Bearer 인증 헤더. 'Bearer {jwtToken}'");
    }

    @Test
    void testGetMembers() throws Exception {
        // given
        String jwtToken = "test-jwt-token";
        MembersQueryDTO.Request request = new MembersQueryDTO.Request();
        request.setSiteName("testSite");
        request.setSearchType("email");
        request.setSearchValue("test");
        request.setPage(0);
        request.setSize(10);

        List<Member> members = List.of(
                Member.builder()
                        .email("test1@test.com")
                        .username("testUser1")
                        .idToken("test-id-token-1")
                        .phoneNumber("010-1234-5678")
                        .carNumber("12가3456")
                        .address(Address.builder()
                                .zipCode("12345")
                                .address1("강남구")
                                .address2("테헤란로")
                                .build())
                        .roles(Roles.ROLE_USER)
                        .accountStatus(AccountStatus.ACTIVE)
                        .build());

        Page<Member> memberPage = new PageImpl<>(members, PageRequest.of(0, 10), 1);
        MembersQueryDTO.Response response = MembersQueryDTO.Response.of(memberPage);

        when(membershipService.getMembers(any(MembersQueryDTO.Request.class))).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/members")
                .header("Authorization", "Bearer " + jwtToken)
                .param("siteName", request.getSiteName())
                .param("searchType", request.getSearchType())
                .param("searchValue", request.getSearchValue())
                .param("page", String.valueOf(request.getPage()))
                .param("size", String.valueOf(request.getSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members[0].email").value("test1@test.com"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andDo(document("api-v1-members",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                commonHeaders()),
                        relaxedQueryParameters(
                                parameterWithName("siteName").description("사이트명"),
                                parameterWithName("searchType").description("검색 유형 (email, username, carNumber)"),
                                parameterWithName("searchValue").description("검색어"),
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지 크기 (1-100)")),
                        responseFields(
                                fieldWithPath("members").description("회원 목록"),
                                fieldWithPath("members[].email").description("이메일"),
                                fieldWithPath("members[].username").description("사용자명"),
                                fieldWithPath("members[].idToken").description("회원 ID 토큰"),
                                fieldWithPath("members[].phoneNumber").description("전화번호"),
                                fieldWithPath("members[].carNumber").description("차량번호"),
                                fieldWithPath("members[].address.zipCode").description("우편번호"),
                                fieldWithPath("members[].address.address1").description("주소1"),
                                fieldWithPath("members[].address.address2").description("주소2"),
                                fieldWithPath("members[].role").description("사용자 권한"),
                                fieldWithPath("members[].accountStatus").description("계정 상태"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 요소 수"),
                                fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
                                fieldWithPath("currentPage").description("현재 페이지 번호"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("hasPrevious").description("이전 페이지 존재 여부"),
                                fieldWithPath("first").description("첫 페이지 여부"),
                                fieldWithPath("last").description("마지막 페이지 여부"))));
    }

    @Test
    void testGetMemberByIdToken() throws Exception {
        // given
        String jwtToken = "test-jwt-token";
        String idToken = "test-id-token";
        MemberQueryDTO.Response response = new MemberQueryDTO.Response(Member.builder()
                .email("test@test.com")
                .username("testUser")
                .idToken(idToken)
                .phoneNumber("010-1234-5678")
                .carNumber("12가3456")
                .address(Address.builder()
                        .zipCode("12345")
                        .address1("강남구")
                        .address2("테헤란로")
                        .build())
                .roles(Roles.ROLE_USER)
                .accountStatus(AccountStatus.ACTIVE)
                .build());

        when(membershipService.getMemberByIdToken(idToken)).thenReturn(response);

        mockMvc.perform(get("/api/v1/members/{idToken}", idToken)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idToken").value(response.getIdToken()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andDo(document("api-v1-members-idToken",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                commonHeaders()),
                        pathParameters(
                                parameterWithName("idToken")
                                        .description("회원 ID 토큰")),
                        responseFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("username").description("사용자명"),
                                fieldWithPath("idToken").description("회원 ID 토큰"),
                                fieldWithPath("phoneNumber").description("전화번호"),
                                fieldWithPath("carNumber").description("차량번호"),
                                fieldWithPath("address.zipCode").description("우편번호"),
                                fieldWithPath("address.address1").description("주소1"),
                                fieldWithPath("address.address2").description("주소2"),
                                fieldWithPath("role").description("사용자 권한"),
                                fieldWithPath("accountStatus").description("계정 상태"))));
    }

    @Test
    void testGetChargeHistory() throws Exception {
        // given
        String jwtToken = "test-jwt-token";
        String idToken = "test-id-token";
        LocalDateTime startDatetime = LocalDateTime.of(2024, 3, 1, 0, 0);
        LocalDateTime endDatetime = LocalDateTime.of(2024, 3, 31, 23, 59);

        ChargeHistoryQueryDTO.Response response = new ChargeHistoryQueryDTO.Response(
                List.of(new ChargeHistoryQueryDTO.Response.ChargeHistoryDTO(
                        "테스트 사이트",
                        startDatetime,
                        endDatetime,
                        new BigDecimal("50000"),
                        new BigDecimal("50.5"),
                        idToken,
                        "testUser",
                        "010-1234-5678",
                        Address.builder()
                                .zipCode("12345")
                                .address1("강남구")
                                .address2("테헤란로")
                                .build(),
                        "12가3456",
                        "테스트 충전기")));

        when(chargeInfoService.getChargeHistory(eq(idToken), any(ChargeHistoryQueryDTO.Request.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/members/{idToken}/charge-history", idToken)
                .header("Authorization", "Bearer " + jwtToken)
                .param("startDatetime", "2024-03-01T00:00:00")
                .param("endDatetime", "2024-03-31T23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chargeHistories").isArray())
                .andExpect(jsonPath("$.chargeHistories[0].idToken").value(idToken))
                .andExpect(jsonPath("$.chargeHistories[0].siteName").value("테스트 사이트"))
                .andDo(document("api-v1-members-charge-history",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                commonHeaders()),
                        pathParameters(
                                parameterWithName("idToken")
                                        .description("회원 ID 토큰")),
                        relaxedQueryParameters(
                                parameterWithName("startDatetime")
                                        .description("조회 시작 일시 (yyyy-MM-dd'T'HH:mm:ss)"),
                                parameterWithName("endDatetime")
                                        .description("조회 종료 일시 (yyyy-MM-dd'T'HH:mm:ss)")),
                        responseFields(
                                fieldWithPath("chargeHistories").description("충전 이력 목록"),
                                fieldWithPath("chargeHistories[].siteName").description("충전소명"),
                                fieldWithPath("chargeHistories[].startDatetime").description("충전 시작 시간"),
                                fieldWithPath("chargeHistories[].endDatetime").description("충전 종료 시간"),
                                fieldWithPath("chargeHistories[].totalPrice").description("총 충전 금액(원)"),
                                fieldWithPath("chargeHistories[].totalMeterValue").description("총 충전량(Wh)"),
                                fieldWithPath("chargeHistories[].idToken").description("회원 ID 토큰"),
                                fieldWithPath("chargeHistories[].username").description("사용자명"),
                                fieldWithPath("chargeHistories[].phoneNumber").description("전화번호"),
                                fieldWithPath("chargeHistories[].address.zipCode").description("우편번호"),
                                fieldWithPath("chargeHistories[].address.address1").description("주소1"),
                                fieldWithPath("chargeHistories[].address.address2").description("주소2"),
                                fieldWithPath("chargeHistories[].carNumber").description("차량번호"),
                                fieldWithPath("chargeHistories[].chargerName").description("충전기명"))));
    }
}
