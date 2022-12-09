package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends RestDocsTestSupport {

    @MockBean
    private MemberQueryService memberQueryService;

    @Test
    @DisplayName("회원을 조회하는 테스트")
    void member를_조회한다() throws Exception {

        when(memberQueryService.readMemberInfo(any())).thenReturn(new MemberRankReadResponse(
                1L,
                "김감자",
                "depromeet/team2.png",
                "안녕하세요 김감자입니다.",
                "루키",
                "ranksImage",
                "1000",
                "www.depromeet.com",
                emptyList(),
                emptyList()
        ));

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
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("성공 여부"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                                .description("저장된 회원 id"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                                .description("닉네임"),
                                        fieldWithPath("data.image").type(JsonFieldType.STRING)
                                                .description("회원 프로필 사진"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                                .description("회원 소개"),
                                        fieldWithPath("data.ranks").type(JsonFieldType.STRING)
                                                .description("회원 등급"),
                                        fieldWithPath("data.ranksImage").type(JsonFieldType.STRING)
                                                .description("회원 등급의 사진"),
                                        fieldWithPath("data.likeCount").type(JsonFieldType.STRING)
                                                .description("회원이 보유한 총 좋아요"),
                                        fieldWithPath("data.profileLink").type(JsonFieldType.STRING)
                                                .description("회원의 프로필 링크"),
                                        fieldWithPath("data.givenTalents").type(JsonFieldType.ARRAY)
                                                .description("회원이 가진 재능"),
                                        fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY)
                                                .description("회원이 줄 수 있는 재능")
                                )
                        )
                )
        ;
    }
}
