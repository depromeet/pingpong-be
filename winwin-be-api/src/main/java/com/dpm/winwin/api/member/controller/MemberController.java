package com.dpm.winwin.api.member.controller;

import static com.dpm.winwin.api.common.utils.CookieUtil.ACCESS_TOKEN;
import static com.dpm.winwin.api.common.utils.CookieUtil.REFRESH_TOKEN;

import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.common.utils.CookieUtil;
import com.dpm.winwin.api.member.dto.PingPongMember;
import com.dpm.winwin.api.member.dto.request.MemberDeleteRequest;
import com.dpm.winwin.api.member.dto.request.MemberNicknameRequest;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberDeleteResponse;
import com.dpm.winwin.api.member.dto.response.MemberNicknameResponse;
import com.dpm.winwin.api.member.dto.response.MemberRankReadResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateImageResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.api.member.dto.response.RanksListResponse;
import com.dpm.winwin.api.member.service.MemberCommandService;
import com.dpm.winwin.api.member.service.MemberQueryService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @PatchMapping("/nickname")
    public BaseResponseDto<MemberNicknameResponse> updateMemberNickname(@RequestBody @Valid MemberNicknameRequest memberNicknameRequest,
                                                                        @AuthenticationPrincipal PingPongMember member) {
        return BaseResponseDto.ok(
            memberCommandService.updateMemberNickname(member.getMemberId(), memberNicknameRequest));
    }

    @PostMapping("/image")
    public BaseResponseDto<MemberUpdateImageResponse> updateProfileImage(@RequestPart(name = "image", required = false) MultipartFile multipartFile,
                                                                         @AuthenticationPrincipal PingPongMember member) {
        return BaseResponseDto.ok(memberCommandService.updateProfileImage(member.getMemberId(), multipartFile));
    }

    @GetMapping("/{memberId}")
    public BaseResponseDto<MemberRankReadResponse> readMemberInfo(@PathVariable Long memberId) {
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(memberId));
    }

    @GetMapping("/me")
    public BaseResponseDto<MemberRankReadResponse> currentMemberInfo(@AuthenticationPrincipal PingPongMember member) {
        return BaseResponseDto.ok(memberQueryService.readMemberInfo(member.getMemberId()));
    }

    @PutMapping
    public BaseResponseDto<MemberUpdateResponse> updateMember(@RequestBody @Valid MemberUpdateRequest memberUpdateRequest,
                                                              @AuthenticationPrincipal PingPongMember member) {
        return BaseResponseDto.ok(memberCommandService.updateMember(member.getMemberId(), memberUpdateRequest));
    }

    @GetMapping("/ranks")
    public BaseResponseDto<RanksListResponse> getRankList() {
        RanksListResponse response = memberQueryService.getRankList();
        return BaseResponseDto.ok(response);
    }

    @DeleteMapping("/me")
    public BaseResponseDto<MemberDeleteResponse> deleteMember(@AuthenticationPrincipal PingPongMember member,
                                                              @RequestBody MemberDeleteRequest memberDeleteRequest,
                                                              HttpServletResponse response) {
        MemberDeleteResponse memberDeleteResponse =
            memberCommandService.deleteMember(member.getMemberId(), memberDeleteRequest.content());

        CookieUtil.addCookie(response, REFRESH_TOKEN, "", 0);
        CookieUtil.addCookie(response, ACCESS_TOKEN, "", 0);
        return BaseResponseDto.ok(memberDeleteResponse);
    }
}
