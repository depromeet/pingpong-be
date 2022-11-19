package com.dpm.winwin.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("아직 데이터가 없어서 나중에 사용할 회원 조회 테스트 ")
    public void memberGetTest() throws Exception {
        // 조회 API -> 대상의 데이터가 있어야 작동
        mockMvc.perform(
                        get("/api/v1/member/{memberId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(
                        document("member-controller-test",
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
                                ),
                                responseFields(
                                        fieldWithPath("memberId").description("회원의 id"),
                                        fieldWithPath("nickname").description("회원의 닉네임"),
                                        fieldWithPath("image").description("회원의 이미지"),
                                        fieldWithPath("introductions").description("소개글"),
                                        fieldWithPath("exchangeCount").description("재능 교환 횟수"),
                                        fieldWithPath("profileLink").description("프로필 링크"),
                                        fieldWithPath("givenTalent").description("주고싶은 재능"),
                                        fieldWithPath("takenTalent").description("받고싶은 재능")
                                )
                        )
                )
        ;
    }
}
