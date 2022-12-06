package com.dpm.winwin.api.member.presentation;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.member.application.MemberQueryService;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberQueryService memberQueryService;
    
    @GetMapping("/{memberId}")
    public MemberReadResponse readMemberInfo(@PathVariable Long memberId){
        return memberQueryService.readMemberInfo(memberId);
    }

    @GetMapping("/ranks")
    public BaseResponseDto<RanksListResponse> getRankList() {
        RanksListResponse response = memberQueryService.getRankList();
        return BaseResponseDto.ok(response);
    }
}
