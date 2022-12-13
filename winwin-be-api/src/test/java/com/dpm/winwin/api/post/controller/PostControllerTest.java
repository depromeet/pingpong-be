package com.dpm.winwin.api.post.controller;

import static com.dpm.winwin.api.utils.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dpm.winwin.api.common.response.dto.GlobalPageResponseDto;
import com.dpm.winwin.api.post.dto.request.LinkRequest;
import com.dpm.winwin.api.common.response.dto.GlobalPageResponseDto;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.MyPagePostResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
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
import java.util.stream.Collectors;
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
    void post가_category에_의해_목록_조회된다() throws Exception {
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
    void post가_memberTalent에_의해_목록_조회된다() throws Exception {
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
    void post가_생성된다() throws Exception {
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
    void post가_삭제된다() throws Exception {
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
            put("/api/v1/posts/1")
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

    @Test
    void 단일_post를_조회한다() throws Exception {
        Long postId = 1L;
        Long memberId = 1L;

        PostReadResponse response = PostReadResponse.builder()
            .id(postId)
            .title("재능 재목")
            .content("재능 내용")
            .isShare(false)
            .subCategory("재능 서브 카테고리")
            .links(List.of(new LinkResponse(1L, "www.naver.com"),
                new LinkResponse(2L, "www.google.com")))
            .chatLink("www.chatLink.com")
            .likes(10000)
            .exchangeType(ExchangeType.ANY_TYPE)
            .exchangePeriod(ExchangePeriod.ANY_PERIOD)
            .exchangeTime(ExchangeTime.ANY_TIME)
            .takenContent("나는 이런걸 받고 싶어요")
            .takenTalents(List.of("서브 카테고리 1", "서브카테고리 2"))
            .memberId(memberId)
            .nickname("minsoozz")
            .image("image/apple.png")
            .ranks(Ranks.BEGINNER.getName())
            .isLike(false)
            .build();

        given(postService.get(postId, memberId))
            .willReturn(response);

        ResultActions result = mockMvc.perform(
            get("/api/v1/posts/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("성공 여부"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("저장된 게시물 id"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING)
                        .description("내용"),
                    fieldWithPath("data.isShare").type(JsonFieldType.BOOLEAN)
                        .description("나눔 여부"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("소분류 카테고리 이름"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY)
                        .description("링크").optional(),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING)
                        .description("채팅 링크"),
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
                        .description("교환 시간"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                        .description("작성자 id"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                        .description("작성자 닉네임"),
                    fieldWithPath("data.image").type(JsonFieldType.STRING)
                        .description("이미지 경로"),
                    fieldWithPath("data.ranks").type(JsonFieldType.STRING)
                        .description("랭킹"),
                    fieldWithPath("data.isLike").type(JsonFieldType.BOOLEAN)
                        .description("해당 회원의 게시글 좋아요 여부"),
                    fieldWithPath("data.likes").type(JsonFieldType.NUMBER)
                        .description("좋아요 수")
                )
            ));
    }

    @Test
    void 회원별_post_목록을_조회한다() throws Exception {

        MyPagePostResponse response1 = new MyPagePostResponse(1L, "목록1", "서브카테고리1", false,
            List.of("탤런트1", "탤런트2"), 1000);
        MyPagePostResponse response2 = new MyPagePostResponse(1L, "목록1", "서브카테고리1", false,
            List.of("탤런트1", "탤런트2"), 1000);
        MyPagePostResponse response3 = new MyPagePostResponse(1L, "목록1", "서브카테고리1", false,
            List.of("탤런트1", "탤런트2"), 1000);

        PageImpl<MyPagePostResponse> myPagePostResponses = new PageImpl<>(
            List.of(response1, response2, response3));
        GlobalPageResponseDto<MyPagePostResponse> globalPageResponseDto = GlobalPageResponseDto.of(
            myPagePostResponses);

        given(postService.getAllByMemberId(any(), any()))
            .willReturn(globalPageResponseDto);

        ResultActions result = mockMvc.perform(
            get("/api/v1/posts/members/{id}", 1L));

        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParameters(
                    parameterWithName("size").optional().description("페이지 번호 (0부터 시작)")
                        .attributes(field("type", "Number")),
                    parameterWithName("page").optional().description("한 페이지에서 보여줄 데이터 개수")
                        .attributes(field("type", "Number"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 여부"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                        .description("게시물 id"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                        .description("게시물 제목"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN)
                        .description("공유 여부"),
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING)
                        .description("소분류 카테고리"),
                    fieldWithPath("data.content[].takenTalents").type(JsonFieldType.ARRAY)
                        .description("소분류 카테고리"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.NUMBER)
                        .description("게시물 좋아요 수"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                        .description("전체 데이터 수"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                        .description("전체 페이지 수"),
                    fieldWithPath("data.hasNextPages").type(JsonFieldType.BOOLEAN)
                        .description("다음 페이지 여부")
                )
            ));
    }
}
