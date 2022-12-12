package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberNicknameResponse;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PatchMapping("/nickname")
    public BaseResponseDto<MemberNicknameResponse> updateMemberNickname(
        @Valid @RequestBody MemberNicknameRequest memberNicknameRequest, BindingResult bindingResult) {
        Long memberId = 1L;
        if (bindingResult.hasErrors()) {
            throw new BusinessException(ErrorMessage.INVALID_NICKNAME);
        }
        return BaseResponseDto.ok(memberCommandService.updateMemberNickname(memberId, memberNicknameRequest));
    }

    @PostMapping("/image")
    public BaseResponseDto<MemberReadResponse> uploadImage(
            @RequestPart(name = "image", required = false) MultipartFile file
    ) {
        Long memberId = 1L;
        return BaseResponseDto.ok(null);
    }

    @GetMapping("/{memberId}")
    public BaseResponseDto<MemberRankReadResponse> readMemberInfo(
        @PathVariable Long memberId) {
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(memberId));
    }

    @PutMapping
    public BaseResponseDto<MemberUpdateResponse> updateMember(
        @RequestBody MemberUpdateRequest memberUpdateRequest) {
        Long memberId = 1L;
        return BaseResponseDto.ok(memberCommandService.updateMember(memberId, memberUpdateRequest));
    }

    @GetMapping("/ranks")
    public BaseResponseDto<RanksListResponse> getRankList() {
        RanksListResponse response = memberQueryService.getRankList();
        return BaseResponseDto.ok(response);
    }

    @DeleteMapping("/{memberId}")
    public BaseResponseDto<Long> deleteMember(@PathVariable Long memberId) {
        Long deleteMemberId = memberCommandService.deleteMember(memberId);

        return BaseResponseDto.ok(deleteMemberId);
    }
}
