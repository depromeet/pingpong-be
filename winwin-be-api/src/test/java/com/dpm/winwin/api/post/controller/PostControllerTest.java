package com.dpm.winwin.api.post.controller;

import static com.dpm.winwin.api.utils.RestDocsConfig.field;
import static com.dpm.winwin.domain.entity.post.Likes.changeFormatCountToString;
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
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.MyPagePostResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostCustomizedResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.dto.response.PostResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
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
        List<PostResponse> posts = setPosts();
        PageRequest pageable = PageRequest.of(0, 2);
        int total = posts.size();
        Page<PostResponse> page = new PageImpl<>(posts, pageable, total);
        GlobalPageResponseDto<PostResponse> response = GlobalPageResponseDto.of(page);

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
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING).description("소분류 카테고리 이름"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.STRING).description("게시물 좋아요 수"),
                    fieldWithPath("data.content[].memberId").type(JsonFieldType.NUMBER).description("작성자 id"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                    fieldWithPath("data.content[].image").type(JsonFieldType.STRING).description("작성자 이미지 url"),
                    fieldWithPath("data.content[].ranks").type(JsonFieldType.STRING).description("작성자 등급"),
                    fieldWithPath("data.content[].takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("받고 싶은 재능"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                    fieldWithPath("data.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 수"),
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
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING).description("소분류 카테고리 이름"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.STRING).description("게시물 좋아요 수"),
                    fieldWithPath("data.content[].memberId").type(JsonFieldType.NUMBER).description("작성자 id"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                    fieldWithPath("data.content[].image").type(JsonFieldType.STRING).description("작성자 이미지 url"),
                    fieldWithPath("data.content[].ranks").type(JsonFieldType.STRING).description("작성자 등급"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                    fieldWithPath("data.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 수"),
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
            "자기계발",
            "취업/이직",
            "자소서 · 면접",
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
                    fieldWithPath("chatLink").type(JsonFieldType.STRING).description("채팅 링크"),
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
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).optional().description("링크"),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING).description("채팅 링크"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("링크 id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("링크"),
                    fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("받고 싶은 재능"),
                    fieldWithPath("data.takenContent").type(JsonFieldType.STRING).optional()
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

    private List<PostResponse> setPosts() {
        List<PostResponse> posts = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            PostResponse postResponse = new PostResponse(
                (long) i,
                "제목" + i,
                "UX/UI 디자인",
                false,
                changeFormatCountToString(1234),
                1L,
                "말하는 감자" + i,
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
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
                changeFormatCountToString(1234),
                1L,
                "말하는 감자" + i,
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
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
            "자기계발",
            "취업/이직",
            "자소서 · 면접",
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
                    fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
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
                    fieldWithPath("data.isShare").type(JsonFieldType.BOOLEAN).description("재능 나눔 여부"),
                    fieldWithPath("data.mainCategory").type(JsonFieldType.STRING)
                        .description("대분류 카테고리 이름"),
                    fieldWithPath("data.midCategory").type(JsonFieldType.STRING)
                        .description("중분류 카테고리 이름"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("소분류 카테고리 이름"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).optional().description("링크"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("링크 id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("링크 내용"),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING).description("채팅 링크"),
                    fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("받고 싶은 재능"),
                    fieldWithPath("data.takenContent").type(JsonFieldType.STRING).optional()
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

        PostReadResponse response = new PostReadResponse(
            postId,
            "제목",
            "내용",
            false,
            "자소서 · 면접",
            List.of(new LinkResponse(1L, "www.naver.com"),
                new LinkResponse(2L, "www.google.com")),
            "www.chatLink.com",
            changeFormatCountToString(1234),
            "받고 싶은 재능 내용",
            List.of("액세서리 · 패션소품", "뜨개 · 자수 · 라탄"),
            ExchangeType.ANY_TYPE,
            ExchangePeriod.ANY_PERIOD,
            ExchangeTime.ANY_TIME,
            memberId,
            "말하는 감자",
            "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
            Ranks.BEGINNER.getName(),
            false,
            "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/category/main/mainCategory-1.png"
        );

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
                pathParameters(
                    parameterWithName("id").description("조회할 게시물 id")
                ),
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
                        .description("재능 나눔 여부"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("소분류 카테고리 이름"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).optional()
                        .description("링크"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("링크 id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("링크 내용"),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING)
                        .description("채팅 링크"),
                    fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("받고 싶은 재능"),
                    fieldWithPath("data.takenContent").type(JsonFieldType.STRING).optional()
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
                        .description("이미지 url"),
                    fieldWithPath("data.ranks").type(JsonFieldType.STRING)
                        .description("등급"),
                    fieldWithPath("data.isLike").type(JsonFieldType.BOOLEAN)
                        .description("해당 회원의 게시글 좋아요 여부"),
                    fieldWithPath("data.likes").type(JsonFieldType.STRING)
                        .description("좋아요 수")
                )
            ));
    }

    @Test
    void 회원별_post_목록을_조회한다() throws Exception {

        MyPagePostResponse response1 = new MyPagePostResponse(1L, "미술 나눔해요", "미술", false,
            List.of("네일아트", "종이접기"), changeFormatCountToString(1234));
        MyPagePostResponse response2 = new MyPagePostResponse(1L, "컴공 나눔해요", "개발", false,
            List.of("Java", "node.js"), changeFormatCountToString(1));

        PageImpl<MyPagePostResponse> myPagePostResponses = new PageImpl<>(
            List.of(response1, response2));
        GlobalPageResponseDto<MyPagePostResponse> globalPageResponseDto = GlobalPageResponseDto.of(
            myPagePostResponses);

        given(postService.getAllByMemberId(any(), any()))
            .willReturn(globalPageResponseDto);

        ResultActions result = mockMvc.perform(
            get("/api/v1/posts/members/{id}", 1L));

        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("id").description("회원 id")
                ),
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
                        .description("재능 나눔 여부"),
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING)
                        .description("소분류 카테고리 이름"),
                    fieldWithPath("data.content[].takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("받고 싶은 재능"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.STRING)
                        .description("게시물 좋아요 수"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                        .description("전체 데이터 수"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                        .description("전체 페이지 수"),
                    fieldWithPath("data.pageNumber").type(JsonFieldType.NUMBER)
                        .description("현재 페이지 번호"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                        .description("현재 페이지의 데이터 수"),
                    fieldWithPath("data.hasNextPages").type(JsonFieldType.BOOLEAN)
                        .description("다음 페이지 여부")
                )
            ));
    }
}
