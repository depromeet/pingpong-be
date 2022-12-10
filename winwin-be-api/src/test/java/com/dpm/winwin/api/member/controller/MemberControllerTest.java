package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.dto.response.RanksResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends RestDocsTestSupport {

    @MockBean
    private MemberQueryService memberQueryService;

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

    @Test
    void 랭크_목록을_조회한다() throws Exception {
        // given
        Ranks rank = Ranks.ROOKIE;
        Ranks rank2 = Ranks.BEGINNER;

        RanksListResponse response = RanksListResponse.from(List.of(
            new RanksResponse(rank.getName(), rank.getImage(), rank.getCondition()),
            new RanksResponse(rank2.getName(), rank2.getImage(), rank2.getCondition())
        ));

        // when
        given(memberQueryService.getRankList())
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/v1/members/ranks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.ranks[].name").type(JsonFieldType.STRING).description("랭크 이름"),
                    fieldWithPath("data.ranks[].image").type(JsonFieldType.STRING).description("랭크 이미지"),
                    fieldWithPath("data.ranks[].condition").type(JsonFieldType.STRING).description("랭크 설명문")
                )
            ))
        ;
    }
}
