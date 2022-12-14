package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberNicknameResponse;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateImageResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.INVALID_NICKNAME;
import static com.dpm.winwin.api.common.utils.SecurityUtil.getCurrentMemberId;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PatchMapping("/nickname")
    public BaseResponseDto<MemberNicknameResponse> updateMemberNickname(
        @Valid @RequestBody MemberNicknameRequest memberNicknameRequest, BindingResult bindingResult) {
        Long memberId = getCurrentMemberId();
        if (bindingResult.hasErrors()) {
            throw new BusinessException(ErrorMessage.INVALID_NICKNAME);
        }
        return BaseResponseDto.ok(memberCommandService.updateMemberNickname(memberId, memberNicknameRequest));
    }

    @PostMapping("/image")
    public BaseResponseDto<MemberUpdateImageResponse> updateProfileImage(
        @RequestPart(name = "image", required = false) MultipartFile multipartFile) {
        Long memberId = 1L;
        return BaseResponseDto.ok(memberCommandService.updateProfileImage(memberId, multipartFile));
    }

    @GetMapping("/{memberId}")
    public BaseResponseDto<MemberRankReadResponse> readMemberInfo(
        @PathVariable Long memberId) {
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(memberId));
    }

    @GetMapping
    public BaseResponseDto<MemberRankReadResponse> currentMemberInfo(@AuthenticationPrincipal User user) {
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(Long.parseLong(user.getUsername())));
    }

    @PutMapping
    public BaseResponseDto<MemberUpdateResponse> updateMember(
        @RequestBody final  MemberUpdateRequest memberUpdateRequest) {
        Long memberId = getCurrentMemberId();
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
