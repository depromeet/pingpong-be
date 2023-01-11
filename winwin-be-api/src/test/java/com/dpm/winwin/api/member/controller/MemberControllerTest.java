package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.member.dto.request.MemberDeleteRequest;
import com.dpm.winwin.api.member.dto.response.MemberDeleteResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateImageResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.dto.response.RanksResponse;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberNicknameResponse;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.dto.response.TalentResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.api.utils.WithMockCustomUser;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import java.util.List;
import com.dpm.winwin.api.member.dto.request.MemberNicknameRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.dpm.winwin.api.member.controller.MemberControllerTest.MEMBER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomUser
public class MemberControllerTest extends RestDocsTestSupport {

    public static final String MEMBER_ID = "1";
    @MockBean
    private MemberCommandService memberCommandService;

    @MockBean
    private MemberQueryService memberQueryService;

    @Test
    void member_닉네임을_설정한다() throws Exception {

        Long memberId = Long.parseLong(MEMBER_ID);
        String nickname = "김감자";

        MemberNicknameRequest request = new MemberNicknameRequest(
                nickname
        );

        MemberNicknameResponse response = new MemberNicknameResponse(
                nickname
        );

        // when
        given(memberCommandService.updateMemberNickname(memberId, request))
                .willReturn(response);

        ResultActions result = mockMvc.perform(
                patch("/api/v1/members/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("수정하려는 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("성공 여부"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("수정된 닉네임")
                        )
                ));
    }

    @Test
    void member의_profileImage를_수정한다() throws Exception {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("image",
            "kirby.png",
            MediaType.IMAGE_PNG_VALUE,
            "image".getBytes());

        MemberUpdateImageResponse response = new MemberUpdateImageResponse(
            "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png");

        // when
        given(memberCommandService.updateProfileImage(1L, multipartFile))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            multipart("/api/v1/members/image")
                .file(multipartFile)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParts(
                    partWithName("image").description("수정할 프로필 이미지")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.image").type(JsonFieldType.STRING).description("저장된 회원의 프로필 이미지 url")
                )
            ));
    }

    @Test
    void member를_수정한다() throws Exception {

        Long memberId = Long.parseLong(MEMBER_ID);
        String nickname = "김감자";

        MemberUpdateRequest request = new MemberUpdateRequest(
                nickname,
                "안녕하세요 김감자입니다.",
                "www.depromeet.com",
                List.of(1L, 2L, 3L),
                List.of(4L, 5L, 6L)
        );

        MemberUpdateResponse response = new MemberUpdateResponse(
                nickname,
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                "안녕하세요 김감자입니다.",
                "www.depromeet.com",
                List.of("자소서 · 면접", "취업 · 이직 · 진로", "기획 · PM"),
                List.of("영업 · MD", "보고서 · 발표", "생산성 툴")
        );

        // when
        given(memberCommandService.updateMember(memberId, request))
                .willReturn(response);

        ResultActions result = mockMvc.perform(
                put("/api/v1/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("수정하려는 닉네임"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING)
                                        .description("수정할 회원 소개"),
                                fieldWithPath("profileLink").type(JsonFieldType.STRING)
                                        .description("수정할 회원의 대표 링크"),
                                fieldWithPath("givenTalents").type(JsonFieldType.ARRAY)
                                        .description("수정할 회원이 가진 재능"),
                                fieldWithPath("takenTalents").type(JsonFieldType.ARRAY)
                                        .description("수정할 회원이 줄 수 있는 재능")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("성공 여부"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("data.image").type(JsonFieldType.STRING)
                                        .description("회원 프로필 이미지 url"),
                                fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                        .description("회원 소개"),
                                fieldWithPath("data.profileLink").type(JsonFieldType.STRING)
                                        .description("회원의 대표 링크"),
                                fieldWithPath("data.givenTalents").type(JsonFieldType.ARRAY)
                                        .description("회원이 가진 재능"),
                                fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY)
                                        .description("회원이 줄 수 있는 재능")
                        )
                ));
    }

    @Test
    void member_profile을_조회한다() throws Exception {

        when(memberQueryService.readMemberInfo(any())).thenReturn(new MemberRankReadResponse(
                1L,
                "홍길동",
                "김감자",
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                "안녕하세요 김감자입니다.",
                "루키",
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                "23",
                "www.depromeet.com",
                List.of(new TalentResponse(1L, "자소서·면접"), new TalentResponse(2L, "취업·이직·진로")),
                List.of(new TalentResponse(1L, "자소서·면접"), new TalentResponse(2L, "취업·이직·진로"))
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
                                        parameterWithName("memberId").description("조회할 회원 id")
                                ),
                                responseFields(
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("성공 여부"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                                .description("저장된 회원 id"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                                .description("유저 이름"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                                .description("닉네임"),
                                        fieldWithPath("data.image").type(JsonFieldType.STRING)
                                                .description("회원 프로필 이미지 url"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                                .description("회원 소개"),
                                        fieldWithPath("data.ranks").type(JsonFieldType.STRING)
                                                .description("회원 등급"),
                                        fieldWithPath("data.ranksImage").type(JsonFieldType.STRING)
                                                .description("회원 등급의 이미지 url"),
                                        fieldWithPath("data.likeCount").type(JsonFieldType.STRING)
                                                .description("회원이 보유한 총 좋아요"),
                                        fieldWithPath("data.profileLink").type(JsonFieldType.STRING)
                                                .description("회원의 대표 링크"),
                                        fieldWithPath("data.givenTalents[].id").type(JsonFieldType.NUMBER)
                                            .description("회원이 가진 재능 id"),
                                        fieldWithPath("data.givenTalents[].content").type(JsonFieldType.STRING)
                                            .description("회원이 가진 재능 내용"),
                                        fieldWithPath("data.takenTalents[].id").type(JsonFieldType.NUMBER)
                                            .description("회원이 줄 수 있는 재능 id"),
                                        fieldWithPath("data.takenTalents[].content").type(JsonFieldType.STRING)
                                            .description("회원이 줄 수 있는 재능 내용")
                                )
                        )
                )
        ;
    }

    @Test
    void rank_목록을_조회한다() throws Exception {
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

    @Test
    void 회원_탈퇴() throws Exception {

        // Given
        Long deleteMemberId = Long.valueOf(MEMBER_ID);
        MemberDeleteResponse memberDeleteResponse = new MemberDeleteResponse(deleteMemberId);
        String description = "탈퇴 테스트";
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(description);
        given(memberCommandService.deleteMember(deleteMemberId, description)).willReturn(memberDeleteResponse);

        // Then
        ResultActions resultActions = mockMvc.perform(
            delete("/api/v1/members/me")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDeleteRequest))
        );

        // Then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.deleteMemberId").type(JsonFieldType.NUMBER).description("탈퇴한 회원 아이디")
                )
            ));
    }
}
