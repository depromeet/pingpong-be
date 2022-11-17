package com.dpm.winwin.api.member.presentation;

import com.dpm.winwin.api.member.application.MemberQueryService;
import com.dpm.winwin.domain.repository.member.dto.MemberDetailResponse;
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
    public MemberDetailResponse getMemberDetailInfo(@PathVariable long memberId){
        return memberQueryService.findMemberDetailInfo(memberId);
    }
}
