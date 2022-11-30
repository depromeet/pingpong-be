package com.dpm.winwin.api.member.controller;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import com.dpm.winwin.api.member.dto.request.MemberCreateRequest;
import com.dpm.winwin.api.member.dto.response.MemberCreateResponse;
import com.dpm.winwin.domain.repository.member.dto.request.MemberImageRequest;
import com.dpm.winwin.domain.repository.member.dto.request.MemberNicknameRequest;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PutMapping("/nickname/{memberId}")
    public BaseResponseDto<Long> updateMemberNickname(
            @PathVariable Long memberId,
            @RequestBody MemberNicknameRequest memberNicknameRequest){
        Long id = memberCommandService.updateMemberNickname(memberId, memberNicknameRequest);
        return BaseResponseDto.ok(id);
    }

    @PutMapping("/{memberId}/image")
    public BaseResponseDto<Long> updateMemberImage(
            @PathVariable Long memberId,
            @RequestBody MemberImageRequest memberImageRequest){
        Long id = memberCommandService.updateMemberImage(memberId, memberImageRequest);
        return BaseResponseDto.ok(id);
    }

    @GetMapping("/{memberId}")
    public @ResponseBody BaseResponseDto<MemberReadResponse> readMemberInfo(
            @PathVariable @RequestParam Long memberId){
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(memberId));
    }

    @PostMapping
    public BaseResponseDto<MemberCreateResponse> createMember(
            @RequestBody final MemberCreateRequest memberCreateRequest){
        MemberCreateResponse memberCreateResponse = memberCommandService.createMember(memberCreateRequest);
        return BaseResponseDto.ok(memberCreateResponse);
    }

    @PutMapping("/{memberId}}")
    public BaseResponseDto<MemberUpdateResponse> updateMember(
            @PathVariable Long memberId,
            @RequestBody final  MemberUpdateRequest memberUpdateRequest) {
        return BaseResponseDto.ok(memberCommandService.updateMember(memberId,memberUpdateRequest));
    }
}
