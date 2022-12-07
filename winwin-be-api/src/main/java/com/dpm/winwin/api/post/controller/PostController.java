package com.dpm.winwin.api.post.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.common.response.dto.GlobalPageResponseDto;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.MyPagePostListResponse;
import com.dpm.winwin.api.post.dto.response.MyPagePostResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostCustomizedResponse;
import com.dpm.winwin.api.post.dto.response.PostListResponse;
import com.dpm.winwin.api.post.dto.response.PostMethodsResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
import com.dpm.winwin.api.post.service.PostService;
import com.dpm.winwin.domain.repository.post.dto.request.PostCustomizedConditionRequest;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public BaseResponseDto<GlobalPageResponseDto<PostListResponse>> getPostList(
        PostListConditionRequest condition, Pageable pageable
    ) {
        GlobalPageResponseDto<PostListResponse> response = postService.getPosts(condition, pageable);
        return BaseResponseDto.ok(response);
    }

    @PatchMapping("/{id}")
    public BaseResponseDto<PostUpdateResponse> updatePost(
        @PathVariable Long id, @RequestBody final PostUpdateRequest updateRequest) {
        long memberId = 1L;
        return BaseResponseDto.ok(postService.update(memberId, id, updateRequest));
    }

    @GetMapping("/custom")
    public BaseResponseDto<GlobalPageResponseDto<PostCustomizedResponse>> getCustomPostList(
        PostCustomizedConditionRequest condition, Pageable pageable) {
        Long tempMemberId = 1L;
        GlobalPageResponseDto<PostCustomizedResponse> response = postService
            .getPostsCustomized(tempMemberId, condition, pageable);
        return BaseResponseDto.ok(response);
    }

    @GetMapping("/{id}")
    public BaseResponseDto<PostReadResponse> getPost(@PathVariable Long id) {
        Long memberId = 1L;
        return BaseResponseDto.ok(postService.get(id, memberId));
    }

    @PostMapping
    public BaseResponseDto<PostAddResponse> createPost(
        @RequestHeader("memberId") final long memberId, @RequestBody final PostAddRequest request) {
        PostAddResponse response = postService.save(memberId, request);
        return BaseResponseDto.ok(response);
    }

    @DeleteMapping("/{id}")
    public BaseResponseDto<Long> deletePost(@PathVariable Long id) {
        Long deletedId = postService.delete(id);
        return BaseResponseDto.ok(deletedId);
    }

    @GetMapping("/method")
    public BaseResponseDto<PostMethodsResponse> getPostMethod() {
        PostMethodsResponse response = postService.getPostMethod();
        return BaseResponseDto.ok(response);
    }

    @GetMapping("/members/{memberId}")
    public BaseResponseDto<GlobalPageResponseDto<MyPagePostResponse>> getAllByMemberId(@PathVariable Long memberId,
        Pageable pageable) {
        return BaseResponseDto.ok(postService.getAllByMemberId(memberId, pageable));
    }
}
