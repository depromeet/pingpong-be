package com.dpm.winwin.api.post.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dpm.winwin.api.post.dto.request.LinkRequest;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
import com.dpm.winwin.api.post.service.PostService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
            ExchangeType.ONLINE,
            ExchangePeriod.A_WEEK,
            ExchangeTime.NOON
        );

        // when
        given(postService.save(memberId, request))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            post("/api/v1/posts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson(request))
        );

        // then
        result.andExpect(status().is2xxSuccessful())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("나눔 여부"),
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

    @Test
    void post를_수정한다() throws Exception {

        // given
        Long memberId = 1L;
        List<LinkRequest> links = new ArrayList<>();
        links.add(new LinkRequest(1L, "www.naver.com"));
        links.add(new LinkRequest(2L, "www.google.com"));

        List<Long> takenTalents = new ArrayList<>();
        takenTalents.add(1L);
        takenTalents.add(2L);
        takenTalents.add(3L);

        PostUpdateRequest request = new PostUpdateRequest(
            "제목",
            "내용",
            false,
            1L,
            links,
            takenTalents,
            "www.chatLink.com",
            "받고 싶은 재능 내용",
            ExchangeType.ONLINE,
            ExchangePeriod.A_WEEK,
            ExchangeTime.NOON
        );

        List<LinkResponse> linksResponse = new ArrayList<>();
        linksResponse.add(new LinkResponse(1L, "www.naver.com"));
        linksResponse.add(new LinkResponse(2L, "www.google.com"));

        List<String> takeCategoriesResponse = linksResponse.stream()
            .map(LinkResponse::content)
            .collect(Collectors.toList());

        PostUpdateResponse response = new PostUpdateResponse(
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
            ExchangeType.ONLINE,
            ExchangePeriod.A_WEEK,
            ExchangeTime.NOON
        );

        // when
        given(postService.update(memberId, 1L, request))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            patch("/api/v1/posts/1")
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
                    fieldWithPath("subCategoryId").type(JsonFieldType.NUMBER)
                        .description("소분류 카테고리 id"),
                    fieldWithPath("links").type(JsonFieldType.ARRAY).description("링크"),
                    fieldWithPath("links[].id").type(JsonFieldType.NUMBER)
                        .description("링크 id"),
                    fieldWithPath("links[].content").type(JsonFieldType.STRING)
                        .description("링크 내용"),
                    fieldWithPath("chatLink").type(JsonFieldType.STRING).description("채팅 링크"),
                    fieldWithPath("takenTalents").type(JsonFieldType.ARRAY)
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
