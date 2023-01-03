package com.dpm.winwin.api.category.controller;

import static com.dpm.winwin.api.member.controller.MemberControllerTest.MEMBER_ID;
import static com.dpm.winwin.api.utils.RestDocsConfig.field;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dpm.winwin.api.category.dto.MainCategoryResponse;
import com.dpm.winwin.api.category.dto.MidCategoryOfMainResponse;
import com.dpm.winwin.api.category.dto.MidCategoryResponse;
import com.dpm.winwin.api.category.dto.SubCategoryResponse;
import com.dpm.winwin.api.category.service.CategoryService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WithMockUser(username = MEMBER_ID, authorities = {"ROLE_USER"})
class CategoryControllerTest extends RestDocsTestSupport {

    @MockBean
    CategoryService categoryService;

    @Test
    void main_category를_조회한다() throws Exception {
        // given
        MainCategoryResponse mainCategoryResponse = new MainCategoryResponse(
            1L,
            "자기계발",
            "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
            List.of(new MidCategoryOfMainResponse(1L, "취업/이직"),
                new MidCategoryOfMainResponse(2L, "직무역량")));

        MainCategoryResponse mainCategoryResponse2 = new MainCategoryResponse(
            2L,
            "디자인 · 영상",
            "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
            List.of(new MidCategoryOfMainResponse(5L, "디자인"),
                new MidCategoryOfMainResponse(6L, "영상")));

        // when
        given(categoryService.getAllMainCategories())
            .willReturn(List.of(mainCategoryResponse, mainCategoryResponse2));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/main")
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("메인 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("대분류 카테고리 이름"),
                    fieldWithPath("data[].image").type(STRING).description("대분류 카테고리 이미지 url"),
                    fieldWithPath("data[].midCategories[].id").type(NUMBER)
                        .description("중분류 카테고리 id"),
                    fieldWithPath("data[].midCategories[].name").type(STRING)
                        .description("중분류 카테고리 이름")))
            );
    }

    @Test
    void mid_category를_조회한다() throws Exception {
        // given
        Long mainCategoryId = 1L;
        MidCategoryResponse midCategoryResponse = new MidCategoryResponse(
            1L,
            "취업/이직",
            List.of(new SubCategoryResponse(1L, "자소서 · 면접"),
                new SubCategoryResponse(2L, "취업 · 이직 · 진로")));

        MidCategoryResponse midCategoryResponse2 = new MidCategoryResponse(
            2L,
            "직무역량",
            List.of(new SubCategoryResponse(4L, "기획 · PM"),
                new SubCategoryResponse(5L, "마케팅")));

        // when
        given(categoryService.getAllMidCategories(mainCategoryId))
            .willReturn(Collections.singletonList(midCategoryResponse));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/mid?mainCategoryId={id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParameters(
                    parameterWithName("mainCategoryId").optional().description("대분류 카테고리 id")
                        .attributes(field("type", "Number"))
                ),
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("중분류 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("중분류 카테고리 이름"),
                    fieldWithPath("data[].subCategories[].id").type(NUMBER)
                        .description("소분류 카테고리 id"),
                    fieldWithPath("data[].subCategories[].name").type(STRING)
                        .description("소분류 카테고리 이름")))
            );
    }

    @Test
    void sub_category를_조회한다() throws Exception {
        // given
        Long midCategoryId = 1L;
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse(1L, "자소서 · 면접");
        SubCategoryResponse subCategoryResponse2 = new SubCategoryResponse(2L, "취업 · 이직 · 진로");

        // when
        given(categoryService.getAllSubCategories(midCategoryId))
            .willReturn(List.of(subCategoryResponse, subCategoryResponse2));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/sub?midCategoryId={id}", 1L));

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParameters(
                    parameterWithName("midCategoryId").optional().description("대분류 카테고리 id")
                        .attributes(field("type", "Number"))
                    ),
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("소분류 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("소분류 카테고리 이름")
                )));
    }

    @Test
    void custom_category를_조회한다() throws Exception {
        // given
        Long memberId = 1L;
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse(1L, "자소서 · 면접");
        SubCategoryResponse subCategoryResponse2 = new SubCategoryResponse(2L, "취업 · 이직 · 진로");


        // when
        given(categoryService.getTakenTalentsByMemberId(memberId))
            .willReturn(List.of(subCategoryResponse, subCategoryResponse2));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/custom")
        );

        //then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("소분류 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("소분류 카테고리 이름")
                )));
    }
}
