package com.dpm.winwin.api.post.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.service.PostService;
import com.dpm.winwin.domain.repository.post.dto.PostListCondition;
import com.dpm.winwin.domain.repository.post.dto.PostMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

  private final PostService postService;

  @GetMapping
  public BaseResponseDto<Page<PostMemberDto>> getPostList(PostListCondition condition, Pageable pageable) {
      Page<PostMemberDto> response = postService.getPosts(condition, pageable);
      return BaseResponseDto.ok(response);
  }

  @GetMapping("/{id}")
  public BaseResponseDto<PostReadResponse> getPost(@PathVariable Long id) {
    return BaseResponseDto.ok(postService.get(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponseDto<PostAddResponse> createPost(
      @RequestHeader("memberId") final long memberId, @RequestBody final PostAddRequest request) {
    PostAddResponse response = postService.save(memberId, request);
    return BaseResponseDto.ok(response);
  }

    @DeleteMapping("/{id}")
    public BaseResponseDto<Long> deletePost(
            @PathVariable Long id
    ) {
        Long deletedId = postService.delete(id);
        return BaseResponseDto.ok(deletedId);
    }
}
