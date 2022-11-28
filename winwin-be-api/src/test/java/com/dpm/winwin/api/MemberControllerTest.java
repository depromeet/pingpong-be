package com.dpm.winwin.api;

import com.dpm.winwin.api.member.application.MemberQueryService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends RestDocsTestSupport {


    @Test
    @Disabled
    @DisplayName("아직 데이터가 없어서 나중에 사용할 회원 조회 테스트 ")
    public void member를_조회한다() throws Exception {

        // 조회 API -> 대상의 데이터가 있어야 작동
        mockMvc.perform(
                        get("/api/v1/member/{memberId}", 1L)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
                                ),
                                responseFields(
                                        fieldWithPath("memberId").type(JsonFieldType.STRING).description(1L),
                                        fieldWithPath("exchangeCount").type(JsonFieldType.STRING).description("0"),
                                        fieldWithPath("image").type(JsonFieldType.STRING).description("member/1"),
                                        fieldWithPath("introductions").type(JsonFieldType.STRING).description("memberIntro"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("memberNickname"),
                                        fieldWithPath("profileLink").type(JsonFieldType.STRING).description("www.depromeet.com")
                                )
                        )
                )
        ;
    }
}
