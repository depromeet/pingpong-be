package com.dpm.winwin.api.post.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.post.dto.response.LikesResponse;
import com.dpm.winwin.api.post.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/{postId}")
    public BaseResponseDto<LikesResponse> likes(@PathVariable Long postId) {
        long tempMemberId = 1L;
        return BaseResponseDto.ok(likesService.createLikes(tempMemberId, postId));
    }

    @DeleteMapping("/{postId}")
    public BaseResponseDto<LikesResponse> unlikes(@PathVariable Long postId) {
        long tempMemberId = 1L;
        return BaseResponseDto.ok(likesService.deleteLikes(tempMemberId, postId));
    }
}
