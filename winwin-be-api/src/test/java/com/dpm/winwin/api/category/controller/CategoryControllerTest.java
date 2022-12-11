package com.dpm.winwin.api.category.controller;

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
import org.springframework.test.web.servlet.ResultActions;

class CategoryControllerTest extends RestDocsTestSupport {

    @MockBean
    CategoryService categoryService;

    @Test
    void main_category를_조회한다() throws Exception {

        MainCategoryResponse mainCategoryResponse = new MainCategoryResponse(1L, "개발",
            "image/hello.jpg", List.of(new MidCategoryOfMainResponse(1L, "백엔드")));

        given(categoryService.getAllMainCategories())
            .willReturn(Collections.singletonList(mainCategoryResponse));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/main")
        );

        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("메인 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("메인 카테고리명"),
                    fieldWithPath("data[].image").type(STRING).description("메인 카테고리 이미지"),
                    fieldWithPath("data[].midCategories[].id").type(NUMBER)
                        .description("미드 카테고리 id"),
                    fieldWithPath("data[].midCategories[].name").type(STRING)
                        .description("미드 카테고리명")))
            );
    }

    @Test
    void mid_category를_조회한다() throws Exception {

        Long mainCategoryId = 1L;
        MidCategoryResponse midCategoryResponse = new MidCategoryResponse(1L, "개발",
            List.of(new SubCategoryResponse(1L, "백엔드")));

        given(categoryService.getAllMidCategories(mainCategoryId))
            .willReturn(Collections.singletonList(midCategoryResponse));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/mid?mainCategoryId=1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParameters(
                    parameterWithName("mainCategoryId").description("대분류 아이디").optional()
                ),
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("미드 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("미드 카테고리명"),
                    fieldWithPath("data[].subCategories[].id").type(NUMBER)
                        .description("서브 카테고리 id"),
                    fieldWithPath("data[].subCategories[].name").type(STRING)
                        .description("서브 카테고리명")))
            );
    }

    @Test
    void sub_category를_조회한다() throws Exception {
        Long midCategoryId = 1L;
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse(1L, "백엔드");

        given(categoryService.getAllSubCategories(midCategoryId))
            .willReturn(Collections.singletonList(subCategoryResponse));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/sub?midCategoryId=1")
        );

        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParameters(
                    parameterWithName("midCategoryId").description("대분류 아이디").optional()
                ),
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("서브 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("서브 카테고리명")
                )));
    }

    @Test
    void custom_category를_조회한다() throws Exception {

        Long memberId = 1L;

        SubCategoryResponse subCategoryResponse = new SubCategoryResponse(1L, "백엔드");

        given(categoryService.getTakenTalentsByMemberId(memberId))
            .willReturn(Collections.singletonList(subCategoryResponse));

        ResultActions result = mockMvc.perform(
            get("/api/v1/categories/custom")
        );

        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(STRING).description("성공 여부"),
                    fieldWithPath("data[].id").type(NUMBER).description("서브 카테고리 id"),
                    fieldWithPath("data[].name").type(STRING).description("서브 카테고리명")
                )));
    }
}