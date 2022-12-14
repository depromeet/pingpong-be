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
    void member_????????????_????????????() throws Exception {

        Long memberId = Long.parseLong(MEMBER_ID);
        String nickname = "?????????";

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
                                        .description("??????????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("?????? ??????"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("????????? ?????????")
                        )
                ));
    }

    @Test
    void member???_profileImage???_????????????() throws Exception {
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
                    partWithName("image").description("????????? ????????? ?????????")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.image").type(JsonFieldType.STRING).description("????????? ????????? ????????? ????????? url")
                )
            ));
    }

    @Test
    void member???_????????????() throws Exception {

        Long memberId = Long.parseLong(MEMBER_ID);
        String nickname = "?????????";

        MemberUpdateRequest request = new MemberUpdateRequest(
                nickname,
                "??????????????? ??????????????????.",
                "www.depromeet.com",
                List.of(1L, 2L, 3L),
                List.of(4L, 5L, 6L)
        );

        MemberUpdateResponse response = new MemberUpdateResponse(
                nickname,
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                "??????????????? ??????????????????.",
                "www.depromeet.com",
                List.of("????????? ?? ??????", "?????? ?? ?????? ?? ??????", "?????? ?? PM"),
                List.of("?????? ?? MD", "????????? ?? ??????", "????????? ???")
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
                                        .description("??????????????? ?????????"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING)
                                        .description("????????? ?????? ??????"),
                                fieldWithPath("profileLink").type(JsonFieldType.STRING)
                                        .description("????????? ????????? ?????? ??????"),
                                fieldWithPath("givenTalents").type(JsonFieldType.ARRAY)
                                        .description("????????? ????????? ?????? ??????"),
                                fieldWithPath("takenTalents").type(JsonFieldType.ARRAY)
                                        .description("????????? ????????? ??? ??? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("?????? ??????"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("?????????"),
                                fieldWithPath("data.image").type(JsonFieldType.STRING)
                                        .description("?????? ????????? ????????? url"),
                                fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                        .description("?????? ??????"),
                                fieldWithPath("data.profileLink").type(JsonFieldType.STRING)
                                        .description("????????? ?????? ??????"),
                                fieldWithPath("data.givenTalents").type(JsonFieldType.ARRAY)
                                        .description("????????? ?????? ??????"),
                                fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY)
                                        .description("????????? ??? ??? ?????? ??????")
                        )
                ));
    }

    @Test
    void member_profile???_????????????() throws Exception {

        when(memberQueryService.readMemberInfo(any())).thenReturn(new MemberRankReadResponse(
                1L,
                "?????????",
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                "??????????????? ??????????????????.",
                "??????",
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                "23",
                "www.depromeet.com",
                List.of(new TalentResponse(1L, "?????????????????"), new TalentResponse(2L, "??????????????????????")),
                List.of(new TalentResponse(1L, "?????????????????"), new TalentResponse(2L, "??????????????????????"))
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
                                        parameterWithName("memberId").description("????????? ?????? id")
                                ),
                                responseFields(
                                        fieldWithPath("message").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                                .description("????????? ?????? id"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                                .description("?????????"),
                                        fieldWithPath("data.image").type(JsonFieldType.STRING)
                                                .description("?????? ????????? ????????? url"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("data.ranks").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("data.ranksImage").type(JsonFieldType.STRING)
                                                .description("?????? ????????? ????????? url"),
                                        fieldWithPath("data.likeCount").type(JsonFieldType.STRING)
                                                .description("????????? ????????? ??? ?????????"),
                                        fieldWithPath("data.profileLink").type(JsonFieldType.STRING)
                                                .description("????????? ?????? ??????"),
                                        fieldWithPath("data.givenTalents[].id").type(JsonFieldType.NUMBER)
                                            .description("????????? ?????? ?????? id"),
                                        fieldWithPath("data.givenTalents[].content").type(JsonFieldType.STRING)
                                            .description("????????? ?????? ?????? ??????"),
                                        fieldWithPath("data.takenTalents[].id").type(JsonFieldType.NUMBER)
                                            .description("????????? ??? ??? ?????? ?????? id"),
                                        fieldWithPath("data.takenTalents[].content").type(JsonFieldType.STRING)
                                            .description("????????? ??? ??? ?????? ?????? ??????")
                                )
                        )
                )
        ;
    }

    @Test
    void rank_?????????_????????????() throws Exception {
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
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.ranks[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.ranks[].image").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("data.ranks[].condition").type(JsonFieldType.STRING).description("?????? ?????????")
                )
            ))
        ;
    }

    @Test
    void ??????_??????() throws Exception {

        // Given
        Long deleteMemberId = Long.valueOf(MEMBER_ID);
        MemberDeleteResponse memberDeleteResponse = new MemberDeleteResponse(deleteMemberId);
        String description = "?????? ?????????";
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
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.deleteMemberId").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????")
                )
            ));
    }
}
