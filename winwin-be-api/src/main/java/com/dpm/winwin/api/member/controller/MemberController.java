package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.common.utils.SecurityUtil;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberNicknameResponse;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.INVALID_NICKNAME;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PatchMapping("/nickname")
    public BaseResponseDto<MemberNicknameResponse> updateMemberNickname(
            @Valid @RequestBody MemberNicknameRequest memberNicknameRequest,
            BindingResult bindingResult) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (bindingResult.hasErrors()){
            throw new BusinessException(INVALID_NICKNAME);
        }
        return BaseResponseDto.ok(memberCommandService.updateMemberNickname(memberId, memberNicknameRequest));
    }

    @GetMapping("/{memberId}")
    public BaseResponseDto<MemberRankReadResponse> readMemberInfo(
            @PathVariable Long memberId) {
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(memberId));
    }

    @PutMapping
    public BaseResponseDto<MemberUpdateResponse> updateMember(
            @RequestBody final  MemberUpdateRequest memberUpdateRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
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
