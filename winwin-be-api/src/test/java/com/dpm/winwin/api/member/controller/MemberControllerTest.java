package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
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
    @DisplayName("아직 데이터가 없어서 나중에 사용할 회원 조회 테스트 ")
    void member를_조회한다() throws Exception {
        when(memberQueryService.readMemberInfo(any())).thenReturn(new MemberRankReadResponse(
                1L,
                "nickname",
                "imageUrl",
                "introduction",
                "ranks",
                "ranksImage",
                1000,
                "profileLink",
                emptyList(),
                emptyList()
        ));

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
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description(1L),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("nickname"),
                                        fieldWithPath("data.image").type(JsonFieldType.STRING).description("member/1"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("memberIntro"),
                                        fieldWithPath("data.ranks").type(JsonFieldType.STRING).description("memberNickname"),
                                        fieldWithPath("data.ranksImage").type(JsonFieldType.STRING).description("www.depromeet.com"),
                                        fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("memberNickname"),
                                        fieldWithPath("data.profileLink").type(JsonFieldType.STRING).description("www.depromeet.com"),
                                        fieldWithPath("data.givenTalents").type(JsonFieldType.ARRAY).description("memberNickname"),
                                        fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY).description("www.depromeet.com")
                                )
                        )
                )
        ;
    }
}
