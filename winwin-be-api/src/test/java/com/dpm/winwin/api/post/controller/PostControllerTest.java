package com.dpm.winwin.api.post.controller;

import static com.dpm.winwin.api.member.controller.MemberControllerTest.MEMBER_ID;
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
import com.dpm.winwin.api.post.dto.response.LikesResponse;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.MyPagePostResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostCustomizedResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.dto.response.PostResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
import com.dpm.winwin.api.post.service.LikesService;
import com.dpm.winwin.api.post.service.PostService;
import com.dpm.winwin.api.utils.RestDocsTestSupport;
import com.dpm.winwin.api.utils.WithMockCustomUser;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WithMockCustomUser
class PostControllerTest extends RestDocsTestSupport {

    @MockBean
    private PostService postService;

    @MockBean
    private LikesService likesService;

    @Test
    void post???_category???_??????_??????_????????????() throws Exception {
        // given
        List<PostResponse> posts = setPosts();
        PageRequest pageable = PageRequest.of(0, 2);
        int total = posts.size();
        Page<PostResponse> page = new PageImpl<>(posts, pageable, total);
        GlobalPageResponseDto<PostResponse> response = GlobalPageResponseDto.of(page);

        // when
        given(postService.getPosts(any(), any(), any()))
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
                    parameterWithName("isShare").optional().description("?????? ?????? ??????")
                        .attributes(field("type", "Boolean")),
                    parameterWithName("mainCategory").optional().description("????????? ???????????? id")
                        .attributes(field("type", "Number")),
                    parameterWithName("midCategory").optional().description("????????? ???????????? id")
                        .attributes(field("type", "Number")),
                    parameterWithName("subCategory").optional().description("????????? ???????????? id")
                        .attributes(field("type", "Number")),
                    parameterWithName("size").optional().description("????????? ?????? (0?????? ??????)")
                        .attributes(field("type", "Number")),
                    parameterWithName("page").optional().description("??? ??????????????? ????????? ????????? ??????")
                        .attributes(field("type", "Number"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("????????? id"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.STRING).description("????????? ????????? ???"),
                    fieldWithPath("data.content[].memberId").type(JsonFieldType.NUMBER).description("????????? id"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
                    fieldWithPath("data.content[].image").type(JsonFieldType.STRING).description("????????? ????????? url"),
                    fieldWithPath("data.content[].ranks").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.content[].takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("?????? ????????? ???"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("?????? ????????? ???"),
                    fieldWithPath("data.pageNumber").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("?????? ???????????? ????????? ???"),
                    fieldWithPath("data.hasNextPages").type(JsonFieldType.BOOLEAN).description("?????? ????????? ??????")
                )
            ));
    }

    @Test
    void post???_memberTalent???_??????_??????_????????????() throws Exception {
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
                    parameterWithName("subCategory").optional().description("????????? ???????????? id")
                        .attributes(field("type", "Number")),
                    parameterWithName("size").optional().description("????????? ?????? (0?????? ??????)")
                        .attributes(field("type", "Number")),
                    parameterWithName("page").optional().description("??? ??????????????? ????????? ????????? ??????")
                        .attributes(field("type", "Number"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.content[].postId").type(JsonFieldType.NUMBER).description("????????? id"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING).description("????????? ???????????? ??????"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.STRING).description("????????? ????????? ???"),
                    fieldWithPath("data.content[].memberId").type(JsonFieldType.NUMBER).description("????????? id"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
                    fieldWithPath("data.content[].image").type(JsonFieldType.STRING).description("????????? ????????? url"),
                    fieldWithPath("data.content[].ranks").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("?????? ????????? ???"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("?????? ????????? ???"),
                    fieldWithPath("data.pageNumber").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("?????? ???????????? ????????? ???"),
                    fieldWithPath("data.hasNextPages").type(JsonFieldType.BOOLEAN).description("?????? ????????? ??????")
                )
            ));
    }

    @Test
    void post???_????????????() throws Exception {
        // given
        Long memberId = 1L;
        List<String> links = Arrays.asList(
            "www.depromeet.com",
            "www.pingpong.com"
        );

        PostAddRequest request = new PostAddRequest(
            "??????",
            "??????",
            false,
            1L,
            links,
            "www.chatLink.com",
            Arrays.asList(1L, 2L, 3L),
            "?????? ?????? ?????? ??????",
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
            "??????",
            "??????",
            false,
            "????????????",
            "??????/??????",
            "????????? ?? ??????",
            linksResponse,
            "www.chatLink.com",
            takeCategoriesResponse,
            "?????? ?????? ?????? ??????",
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
                    fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                    fieldWithPath("subCategoryId").type(JsonFieldType.NUMBER)
                        .description("????????? ???????????? id"),
                    fieldWithPath("links").type(JsonFieldType.ARRAY).description("??????"),
                    fieldWithPath("chatLink").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("takenTalentIds").type(JsonFieldType.ARRAY)
                        .description("?????? ?????? ?????? id"),
                    fieldWithPath("takenContent").type(JsonFieldType.STRING)
                        .description("?????? ?????? ??????"),
                    fieldWithPath("exchangeType").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                    fieldWithPath("exchangePeriod").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                    fieldWithPath("exchangeTime").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("????????? ????????? id"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.isShare").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                    fieldWithPath("data.mainCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.midCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).optional().description("??????"),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("?????? id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.takenContent").type(JsonFieldType.STRING).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.exchangeType").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.exchangePeriod").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.exchangeTime").type(JsonFieldType.STRING)
                        .description("?????? ??????")
                )
            ));
    }

    @Test
    void post???_????????????() throws Exception {
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
                    parameterWithName("id").description("????????? ????????? id")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("????????? ????????? id")
                )
            ));
    }

    private List<PostResponse> setPosts() {
        List<PostResponse> posts = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            PostResponse postResponse = new PostResponse(
                (long) i,
                "??????" + i,
                "UX/UI ?????????",
                false,
                changeFormatCountToString(1234),
                1L,
                "????????? ??????" + i,
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                Ranks.BEGINNER.getName(),
                Arrays.asList("????????? ?????????", "?????????", "????????? ?????????")
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
                "??????" + i,
                "UX/UI ?????????",
                false,
                changeFormatCountToString(1234),
                1L,
                "????????? ??????" + i,
                "https://dpm-pingpong-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/3d4395e461db40108104200e286870c4-kirby.png",
                Ranks.BEGINNER.getName()
            );
            posts.add(postCustomResponse);
        }
        return posts;
    }

    @Test
    void post???_????????????() throws Exception {

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
            "??????",
            "??????",
            false,
            1L,
            links,
            takenTalents,
            "www.chatLink.com",
            "?????? ?????? ?????? ??????",
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
            "??????",
            "??????",
            false,
            "????????????",
            "??????/??????",
            "????????? ?? ??????",
            linksResponse,
            "www.chatLink.com",
            takeCategoriesResponse,
            "?????? ?????? ?????? ??????",
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
                    headerWithName("memberId").description("?????? id")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                    fieldWithPath("subCategoryId").type(JsonFieldType.NUMBER)
                        .description("????????? ???????????? id"),
                    fieldWithPath("links").type(JsonFieldType.ARRAY).description("??????"),
                    fieldWithPath("links[].id").type(JsonFieldType.NUMBER)
                        .description("?????? id"),
                    fieldWithPath("links[].content").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("chatLink").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("takenTalents").type(JsonFieldType.ARRAY)
                        .description("?????? ?????? ?????? id"),
                    fieldWithPath("takenContent").type(JsonFieldType.STRING)
                        .description("?????? ?????? ??????"),
                    fieldWithPath("exchangeType").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("exchangePeriod").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("exchangeTime").type(JsonFieldType.STRING).description("?????? ??????")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("????????? ????????? id"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("data.isShare").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                    fieldWithPath("data.mainCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.midCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).optional().description("??????"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("?????? id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.takenContent").type(JsonFieldType.STRING).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.exchangeType").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.exchangePeriod").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.exchangeTime").type(JsonFieldType.STRING)
                        .description("?????? ??????")
                )
            ));
    }

    @Test
    void ??????_post???_????????????() throws Exception {
        Long postId = 1L;
        Long memberId = 1L;

        PostReadResponse response = new PostReadResponse(
            postId,
            "??????",
            "??????",
            false,
            "????????? ?? ??????",
            List.of(new LinkResponse(1L, "www.naver.com"),
                new LinkResponse(2L, "www.google.com")),
            "www.chatLink.com",
            changeFormatCountToString(1234),
            "?????? ?????? ?????? ??????",
            List.of("???????????? ?? ????????????", "?????? ?? ?????? ?? ??????"),
            ExchangeType.ANY_TYPE,
            ExchangePeriod.ANY_PERIOD,
            ExchangeTime.ANY_TIME,
            memberId,
            "????????? ??????",
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
                    parameterWithName("id").description("????????? ????????? id")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("????????? ????????? id"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("data.isShare").type(JsonFieldType.BOOLEAN)
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.subCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.links").type(JsonFieldType.ARRAY).optional()
                        .description("??????"),
                    fieldWithPath("data.links[].id").type(JsonFieldType.NUMBER)
                        .description("?????? id"),
                    fieldWithPath("data.links[].content").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.chatLink").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.takenContent").type(JsonFieldType.STRING).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.exchangeType").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.exchangePeriod").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.exchangeTime").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                        .description("????????? id"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                        .description("????????? ?????????"),
                    fieldWithPath("data.image").type(JsonFieldType.STRING)
                        .description("????????? url"),
                    fieldWithPath("data.ranks").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("data.isLike").type(JsonFieldType.BOOLEAN)
                        .description("?????? ????????? ????????? ????????? ??????"),
                    fieldWithPath("data.likes").type(JsonFieldType.STRING)
                        .description("????????? ???"),
                    fieldWithPath("data.backgroundImage").type(JsonFieldType.STRING)
                        .description("?????? ????????? url")
                )
            ));
    }

    @Test
    void ?????????_post_?????????_????????????() throws Exception {

        MyPagePostResponse response1 = new MyPagePostResponse(1L, "?????? ????????????", "??????", false,
            List.of("????????????", "????????????"), changeFormatCountToString(1234));
        MyPagePostResponse response2 = new MyPagePostResponse(1L, "?????? ????????????", "??????", false,
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
                    parameterWithName("id").description("?????? id")
                ),
                requestParameters(
                    parameterWithName("size").optional().description("????????? ?????? (0?????? ??????)")
                        .attributes(field("type", "Number")),
                    parameterWithName("page").optional().description("??? ??????????????? ????????? ????????? ??????")
                        .attributes(field("type", "Number"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                        .description("????????? id"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                        .description("????????? ??????"),
                    fieldWithPath("data.content[].isShare").type(JsonFieldType.BOOLEAN)
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.content[].subCategory").type(JsonFieldType.STRING)
                        .description("????????? ???????????? ??????"),
                    fieldWithPath("data.content[].takenTalents").type(JsonFieldType.ARRAY).optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.content[].likes").type(JsonFieldType.STRING)
                        .description("????????? ????????? ???"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                        .description("?????? ????????? ???"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                        .description("?????? ????????? ???"),
                    fieldWithPath("data.pageNumber").type(JsonFieldType.NUMBER)
                        .description("?????? ????????? ??????"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                        .description("?????? ???????????? ????????? ???"),
                    fieldWithPath("data.hasNextPages").type(JsonFieldType.BOOLEAN)
                        .description("?????? ????????? ??????")
                )
            ));
    }

    @Test
    public void post???_like???_???????????????() throws Exception {
        // given
        Long memberId = 1L;
        Long postId = 1L;

        LikesResponse likesResponse = new LikesResponse(5);

        // when
        given(likesService.createLikes(memberId, postId))
            .willReturn(likesResponse);

        ResultActions result = mockMvc.perform(
            post("/api/v1/posts/{postId}/likes", postId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("postId").description("????????? ?????? ????????? ????????? id")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("?????? ???????????? ????????? ???")
                )
            ));
    }

    @Test
    public void post???_like???_????????????() throws Exception {
        // given
        Long memberId = 1L;
        Long postId = 1L;

        LikesResponse likesResponse = new LikesResponse(5);

        // when
        given(likesService.cancelLikes(memberId, postId))
            .willReturn(likesResponse);

        ResultActions result = mockMvc.perform(
            post("/api/v1/posts/{postId}/unlikes", postId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("postId").description("???????????? ????????? ????????? id")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
                    fieldWithPath("data.likes").type(JsonFieldType.NUMBER).description("?????? ???????????? ????????? ???")
                )
            ));
    }
}
