package com.ev.ocpp16.domain.member.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.common.exception.ApiExceptionStatus;
import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.entity.enums.AccountStatus;
import com.ev.ocpp16.domain.member.entity.enums.Address;
import com.ev.ocpp16.domain.member.entity.enums.Roles;
import com.ev.ocpp16.domain.member.repository.MemberRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Nested
    @DisplayName("회원 조회 API 테스트")
    class GetMemberTests {

        @BeforeEach
        void setUp() throws Exception {
            // 테스트 주소 생성
            Address testAddress = new Address();
            setField(testAddress, "zipCode", "07666");
            setField(testAddress, "address1", "테스트 주소1");
            setField(testAddress, "address2", "테스트 주소2");

            // 테스트 회원 생성
            testMember = new Member();
            setField(testMember, "idToken", "TEST-ID-TOKEN");
            setField(testMember, "username", "테스트사용자");
            setField(testMember, "password", "1234");
            setField(testMember, "email", "test@example.com");
            setField(testMember, "phoneNumber", "010-1234-5678");
            setField(testMember, "carNumber", "01마3456");
            setField(testMember, "address", testAddress);
            setField(testMember, "roles", Roles.ROLE_USER);
            setField(testMember, "accountStatus", AccountStatus.ACTIVE);

            memberRepository.save(testMember);
        }

        @Test
        @DisplayName("성공: 회원 조회 API")
        void getMemberByIdToken_Success() throws Exception {
            // when
            ResultActions result = mockMvc.perform(
                    get("/api/v1/members/{idToken}", testMember.getIdToken())
                            .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.idToken").value(testMember.getIdToken()))
                    .andExpect(jsonPath("$.username").value(testMember.getUsername()))
                    .andExpect(jsonPath("$.email").value(testMember.getEmail()))
                    .andExpect(jsonPath("$.phoneNumber").value(testMember.getPhoneNumber()))
                    .andExpect(jsonPath("$.carNumber").value(testMember.getCarNumber()))
                    .andExpect(jsonPath("$.address.zipCode").value(testMember.getAddress().getZipCode()))
                    .andExpect(jsonPath("$.address.address1").value(testMember.getAddress().getAddress1()))
                    .andExpect(jsonPath("$.address.address2").value(testMember.getAddress().getAddress2()))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 존재하지 않는 회원")
        void getMemberByIdToken_NotFound() throws Exception {
            // when
            ResultActions result = mockMvc.perform(
                    get("/api/v1/members/{idToken}", "INV-ID-TOKEN")
                            .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(ApiExceptionStatus.NOT_FOUND_MEMBER.getResultCode()))
                    .andExpect(jsonPath("$.detail").value(ApiExceptionStatus.NOT_FOUND_MEMBER.getResultMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패:유효성 검사")
        void getMemberByIdToken_ValidationError() throws Exception {
            String invalidIdToken = "1".repeat(37);

            // when
            ResultActions result = mockMvc.perform(
                    get("/api/v1/members/{idToken}", invalidIdToken)
                            .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }
}
