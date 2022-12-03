package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PutMapping("/nickname")
    public BaseResponseDto<Long> updateMemberNickname(
            @RequestBody MemberNicknameRequest memberNicknameRequest){
        Long memberId = 1L; //임시로 특정 회원 고정
        Long id = memberCommandService.updateMemberNickname(memberId, memberNicknameRequest);
        return BaseResponseDto.ok(id);
    }

    @GetMapping("/{memberId}")
    public BaseResponseDto<MemberRankReadResponse> readMemberInfo(
            @PathVariable Long memberId){
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(memberId));
    }

    @PutMapping("/{memberId}")
    public BaseResponseDto<MemberUpdateResponse> updateMember(
            @PathVariable Long memberId,
            @RequestBody final  MemberUpdateRequest memberUpdateRequest) {
        return BaseResponseDto.ok(memberCommandService.updateMember(memberId,memberUpdateRequest));
    }

    @GetMapping("/ranks")
    public BaseResponseDto<RanksListResponse> getRankList() {
        RanksListResponse response = memberQueryService.getRankList();
        return BaseResponseDto.ok(response);
    }
}
