package com.dpm.winwin.api.post.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.service.PostService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class PostControllerTest extends RestDocsTestSupport {

    @MockBean
    private PostService postService;

    @Test
    public void post가_생성된다() throws Exception {
        // given
        Long memberId = 1L;
        List<String> links = Arrays.asList(
            "www.depromeet.com",
            "www.pingpong.com"
        );

        PostAddRequest request = new PostAddRequest(
            "제목",
            "내용",
            false,
            1L,
            2L,
            1L,
            links,
            "www.chatLink.com",
            Arrays.asList(1L, 2L, 3L),
            "받고 싶은 재능 내용",
            ExchangeType.ONLINE,
            ExchangePeriod.A_WEEK,
            ExchangeTime.NOON
        );

        List<LinkResponse> linksResponse = Arrays
            .asList(new LinkResponse(1L, "www.depromeet.com"),
                new LinkResponse(2L, "www.pingpong.com"));
        List<String> takeCategoriesResponse = linksResponse.stream()
            .map(LinkResponse::content)
            .toList();

        PostAddResponse response = new PostAddResponse(
            1L,
            "제목",
            "내용",
            false,
            "대분류 카테고리 이름",
            "중분류 카테고리 이름",
            "소분류 카테고리 이름",
            linksResponse,
            "www.chatLink.com",
            takeCategoriesResponse,
            "받고 싶은 재능 내용",
            ExchangeType.ONLINE.getMessage(),
            ExchangePeriod.A_WEEK.getMessage(),
            ExchangeTime.NOON.getMessage()
        );

        // when
        given(postService.save(memberId, request))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/v1/posts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                    "memberId",
                    1
                )
                .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName("memberId").description("회원 id")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("나눔 여부"),
                    fieldWithPath("mainCategoryId").type(JsonFieldType.NUMBER)
                        .description("대분류 카테고리 id"),
                    fieldWithPath("midCategoryId").type(JsonFieldType.NUMBER)
                        .description("중분류 카테고리 id"),
                    fieldWithPath("subCategoryId").type(JsonFieldType.NUMBER)
                        .description("소분류 카테고리 id"),
                    fieldWithPath("links").type(JsonFieldType.ARRAY).description("링크"),
                    fieldWithPath("chatLink").type(JsonFieldType.STRING).description("채팅 링크"),
                    fieldWithPath("takenTalentIds").type(JsonFieldType.ARRAY)
                        .description("받고 싶은 재능 id"),
                    fieldWithPath("takenContent").type(JsonFieldType.STRING)
                        .description("받고 싶은 내용"),
                    fieldWithPath("exchangeType").type(JsonFieldType.STRING).description("교환 방식"),
                    fieldWithPath("exchangePeriod").type(JsonFieldType.STRING).description("교환 기간"),
                    fieldWithPath("exchangeTime").type(JsonFieldType.STRING).description("교환 시간")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("저장된 게시물 id"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.isShare").type(JsonFieldType.BOOLEAN).description("나눔 여부"),
                    fieldWithPath("data.mainCategory").type(JsonFieldType.STRING)
                        .description("대분류 카테고리 이름"),
                    fieldWithPath("data.midCategory").type(JsonFieldType.STRING)
                        .description("중분류 카테고리 이름"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("소분류 카테고리 이름"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).description("링크")
                        .optional(),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING).description("채팅 링크"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("링크 id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("링크 내용"),
                    fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY)
                        .description("받고 싶은 카테고리"),
                    fieldWithPath("data.takenContent").type(JsonFieldType.STRING)
                        .description("받고 싶은 내용"),
                    fieldWithPath("data.exchangeType").type(JsonFieldType.STRING)
                        .description("교환 방식"),
                    fieldWithPath("data.exchangePeriod").type(JsonFieldType.STRING)
                        .description("교환 기간"),
                    fieldWithPath("data.exchangeTime").type(JsonFieldType.STRING)
                        .description("교환 시간")
                )
            ));
    }
}
