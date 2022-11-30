package com.dpm.winwin.api;

import static org.mockito.BDDMockito.given;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.dto.request.MemberCreateRequest;
import com.dpm.winwin.api.member.dto.response.MemberCreateResponse;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends RestDocsTestSupport {

    @MockBean
    private MemberCommandService memberCommandService;

    @Disabled
    @Test
    void member가_생성된다() throws Exception {

        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "김감자",
                null,"hello",0,null
        );

        MemberCreateResponse memberCreateResponse = new MemberCreateResponse(
                1L,
                "김감자"
        );

        given(memberCommandService.createMember(memberCreateRequest))
                .willReturn(memberCreateResponse);

        ResultActions result = mockMvc.perform(
                post("/api/v1/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(memberCreateRequest))
        );

        result.andExpect(status().is2xxSuccessful())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("저장된 닉네임 id"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("저장된 회원 닉네임"),
                                fieldWithPath("data.image").type(JsonFieldType.STRING).description("저장된 회원 이미지"),
                                fieldWithPath("data.introductions").type(JsonFieldType.STRING).description("저장된 회원 소개"),
                                fieldWithPath("data.exchangeCount").type(JsonFieldType.NUMBER).description("저장된 회원 교환횟수"),
                                fieldWithPath("data.profileLink").type(JsonFieldType.STRING).description("저장된 회원 프로필 링크")
                                )
                        ));
    }



    @Test
    @Disabled
    @DisplayName("아직 데이터가 없어서 나중에 사용할 회원 조회 테스트 ")
    void member를_조회한다() throws Exception {

        // 조회 API -> 대상의 데이터가 있어야 작동
        mockMvc.perform(
                        get("/api/v1/members/{memberId}", 1L)
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
