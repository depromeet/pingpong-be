package com.dpm.winwin.api.auth.controller;

import com.dpm.winwin.api.auth.service.AuthService;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.member.dto.PingPongMember;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/logout")
    public BaseResponseDto<Long> logout(@AuthenticationPrincipal PingPongMember member,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        authService.logout(request, response);
        return BaseResponseDto.message(member.getMemberId(), "LOGOUT SUCCESS");
    }
}
