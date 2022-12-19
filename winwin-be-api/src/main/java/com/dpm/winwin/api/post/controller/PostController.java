package com.dpm.winwin.api.post.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.common.response.dto.GlobalPageResponseDto;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.MyPagePostResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostCustomizedResponse;
import com.dpm.winwin.api.post.dto.response.PostMethodsResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.dto.response.PostResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
import com.dpm.winwin.api.post.service.PostService;
import com.dpm.winwin.domain.repository.post.dto.request.PostCustomizedConditionRequest;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public BaseResponseDto<GlobalPageResponseDto<PostResponse>> getPosts(
        PostListConditionRequest condition, Pageable pageable) {
        return BaseResponseDto.ok(postService.getPosts(condition, pageable));
    }

    @GetMapping("/custom")
    public BaseResponseDto<GlobalPageResponseDto<PostCustomizedResponse>> getCustomPosts(
        PostCustomizedConditionRequest condition, Pageable pageable) {
        Long tempMemberId = 1L;
        return BaseResponseDto.ok(postService.getPostsCustomized(tempMemberId, condition, pageable));
    }

    @GetMapping("/{id}")
    public BaseResponseDto<PostReadResponse> getPost(@PathVariable Long id) {
        Long memberId = 1L;
        return BaseResponseDto.ok(postService.get(id, memberId));
    }

    @PostMapping
    public BaseResponseDto<PostAddResponse> createPost(@RequestBody @Valid PostAddRequest request) {
        long tempMemberId = 1L;
        return BaseResponseDto.ok(postService.save(tempMemberId, request));
    }

    @PutMapping("/{id}")
    public BaseResponseDto<PostUpdateResponse> updatePost(
        @PathVariable Long id, @RequestBody @Valid final PostUpdateRequest updateRequest) {
        long memberId = 1L;
        return BaseResponseDto.ok(postService.update(memberId, id, updateRequest));
    }


    @DeleteMapping("/{id}")
    public BaseResponseDto<Long> deletePost(@PathVariable Long id) {
        return BaseResponseDto.ok(postService.delete(id));
    }

    @GetMapping("/method")
    public BaseResponseDto<PostMethodsResponse> getPostMethods() {
        return BaseResponseDto.ok(postService.getPostMethods());
    }

    @GetMapping("/members/{memberId}")
    public BaseResponseDto<GlobalPageResponseDto<MyPagePostResponse>> getAllByMemberId(
        @PathVariable Long memberId, Pageable pageable) {
        return BaseResponseDto.ok(postService.getAllByMemberId(memberId, pageable));
    }
}
