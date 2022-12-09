package com.dpm.winwin.api.post.controller;

import static com.dpm.winwin.api.utils.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dpm.winwin.api.common.response.dto.GlobalPageResponseDto;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostCustomizedResponse;
import com.dpm.winwin.api.post.dto.response.PostListResponse;
import com.dpm.winwin.api.post.service.PostService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class PostControllerTest extends RestDocsTestSupport {

    @MockBean
    private PostService postService;

    @Test
    public void post가_category에_의해_목록_조회된다() throws Exception {
        // given
        List<PostListResponse> posts = setPosts();
        PageRequest pageable = PageRequest.of(0, 2);
        int total = posts.size();
        Page<PostListResponse> page = new PageImpl<>(posts, pageable, total);
        GlobalPageResponseDto<PostListResponse> response = GlobalPageResponseDto.of(page);

        // when
        given(postService.getPosts(any(), any()))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/v1/posts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParameters(
                    parameterWithName("isShare").optional().description("재능 나눔 여부")
                        .attributes(field("type", "Boolean")),
                    parameterWithName("mainCategory").optional().description("대분류 카테고리 id")
                        .attributes(field("type", "Number")),
                    parameterWithName("midCategory").optional().description("중분류 카테고리 id")
                        .attributes(field("type", "Number")),
                    parameterWithName("subCategory").optional().description("소분류 카테고리 id")
                        .attributes(field("type", "Number")),
                    parameterWithName("size").optional().description("페이지 번호 (0부터 시작)")
                        .attributes(field("type", "Number")),
                    parameterWithName("page").optional().description("한 페이지에서 보여줄 데이터 개수")
                        .attributes(field("type", "Number"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("게시물 id"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING).description("소분류 카테고리"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.NUMBER).description("게시물 좋아요 수"),
                    fieldWithPath("data.content[].memberId").type(JsonFieldType.NUMBER).description("작성자 id"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                    fieldWithPath("data.content[].image").type(JsonFieldType.STRING).description("작성자 이미지 url"),
                    fieldWithPath("data.content[].ranks").type(JsonFieldType.STRING).description("작성자 등급"),
                    fieldWithPath("data.content[].takenTalents").type(JsonFieldType.ARRAY).description("받고 싶은 재능"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                    fieldWithPath("data.hasNextPages").type(JsonFieldType.BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    public void post가_memberTalent에_의해_목록_조회된다() throws Exception {
        // given
        List<PostCustomizedResponse> posts = setCustomPosts();
        PageRequest pageable = PageRequest.of(0, 2);
        int total = posts.size();
        Page<PostCustomizedResponse> page = new PageImpl<>(posts, pageable, total);
        GlobalPageResponseDto<PostCustomizedResponse> response = GlobalPageResponseDto.of(page);

        // when
        given(postService.getPostsCustomized(any(), any(), any()))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/v1/posts/custom")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParameters(
                    parameterWithName("subCategory").optional().description("소분류 카테고리 id")
                        .attributes(field("type", "Number")),
                    parameterWithName("size").optional().description("페이지 번호 (0부터 시작)")
                        .attributes(field("type", "Number")),
                    parameterWithName("page").optional().description("한 페이지에서 보여줄 데이터 개수")
                        .attributes(field("type", "Number"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.content[].postId").type(JsonFieldType.NUMBER).description("게시물 id"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING).description("소분류 카테고리"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.NUMBER).description("게시물 좋아요 수"),
                    fieldWithPath("data.content[].memberId").type(JsonFieldType.NUMBER).description("작성자 id"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                    fieldWithPath("data.content[].image").type(JsonFieldType.STRING).description("작성자 이미지 url"),
                    fieldWithPath("data.content[].ranks").type(JsonFieldType.STRING).description("작성자 등급"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                    fieldWithPath("data.hasNextPages").type(JsonFieldType.BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

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
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용"),
                    fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
                    fieldWithPath("subCategoryId").type(JsonFieldType.NUMBER)
                        .description("소분류 카테고리 id"),
                    fieldWithPath("links").type(JsonFieldType.ARRAY).description("링크"),
                    fieldWithPath("chatLink").type(JsonFieldType.STRING).description("오픈 채팅 링크"),
                    fieldWithPath("takenTalentIds").type(JsonFieldType.ARRAY)
                        .description("받고 싶은 재능 id"),
                    fieldWithPath("takenContent").type(JsonFieldType.STRING)
                        .description("받고 싶은 내용"),
                    fieldWithPath("exchangeType").type(JsonFieldType.STRING).description("재능 교환 방식"),
                    fieldWithPath("exchangePeriod").type(JsonFieldType.STRING).description("재능 교환 기간"),
                    fieldWithPath("exchangeTime").type(JsonFieldType.STRING).description("재능 교환 시간")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("저장된 게시물 id"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시물 제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시물 내용"),
                    fieldWithPath("data.isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
                    fieldWithPath("data.mainCategory").type(JsonFieldType.STRING)
                        .description("대분류 카테고리 이름"),
                    fieldWithPath("data.midCategory").type(JsonFieldType.STRING)
                        .description("중분류 카테고리 이름"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("소분류 카테고리 이름"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).description("링크")
                        .optional(),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING).description("오픈 채팅 링크"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("링크 id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("링크"),
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
    public void post가_삭제된다() throws Exception {
        // given
        Long postId = 1L;

        //when
        given(postService.delete(postId))
            .willReturn(postId);

        ResultActions result = mockMvc.perform(
            delete("/api/v1/posts/{id}", postId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("id").description("삭제할 게시물 id")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("삭제된 게시물 id")
                )
            ));
    }

    private List<PostListResponse> setPosts() {
        List<PostListResponse> posts = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            PostListResponse postResponse = new PostListResponse(
                (long) i,
                "제목" + i,
                "UX/UI 디자인",
                false,
                3,
                1L,
                "말하는 감자" + i,
                "imageUrl" + i,
                Ranks.BEGINNER.getName(),
                Arrays.asList("그래픽 디자인", "글쓰기", "브랜드 디자인")
            );
            posts.add(postResponse);
        }
        return posts;
    }

    private List<PostCustomizedResponse> setCustomPosts() {
        List<PostCustomizedResponse> posts = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            PostCustomizedResponse postCustomResponse = new PostCustomizedResponse(
                (long) i,
                "제목" + i,
                "UX/UI 디자인",
                false,
                3,
                1L,
                "말하는 감자" + i,
                "imageUrl" + i,
                Ranks.BEGINNER.getName()
            );
            posts.add(postCustomResponse);
        }
        return posts;
    }
}
