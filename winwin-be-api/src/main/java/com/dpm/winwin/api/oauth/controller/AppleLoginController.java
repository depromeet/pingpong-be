package com.dpm.winwin.api.oauth.controller;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.LoginCancelException;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.common.utils.CookieUtil;
import com.dpm.winwin.api.jwt.TokenResponse;
import com.dpm.winwin.api.oauth.service.AppleLoginService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AppleLoginController {


    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private final AppleLoginService appleLoginService;

    @PostMapping(value = "/apple/redirect", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public BaseResponseDto<TokenResponse> appleRedirect(@RequestBody MultiValueMap<String, String> redirectInfo, HttpServletResponse response) throws IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {
        log.info("----> {}", redirectInfo);

        /**
         * 로그인 후 취소한 경우
         */
        if (!ObjectUtils.isEmpty(redirectInfo.get("error"))) {
            throw new LoginCancelException(ErrorMessage.LOGIN_CANCEL);
        }

        /**
         * 유저가 처음 가입하는 유저일 경우
         */
        if (redirectInfo.containsKey("user")) {
            String memberInfo = redirectInfo.getFirst("user");
            String code = redirectInfo.getFirst("code");

            log.info("memberInfo:: {}", memberInfo);
            TokenResponse token = appleLoginService.signUpMember(memberInfo, code);
            setTokenCookie(response, token);
            return BaseResponseDto.ok(token);
        }

        /**
         * 기존 회원이 로그인 한 경우
         */
        log.info("authorization :: {}", redirectInfo.get("code"));
        log.info("authorization :: {}", redirectInfo.get("state"));
        log.info("authorization :: {}", redirectInfo.get("id_token"));
        log.info("authorization :: {}", redirectInfo.get("user"));
        log.info("error :: {} ", redirectInfo.get("error"));

        String code = redirectInfo.getFirst("code");
        TokenResponse token = appleLoginService.signInMember(code);

        setTokenCookie(response, token);
        return BaseResponseDto.ok(token);
    }

    private void setTokenCookie(HttpServletResponse response, TokenResponse token) {
        CookieUtil.addCookie(response, ACCESS_TOKEN, token.accessToken(), 86400);
        CookieUtil.addCookie(response, REFRESH_TOKEN, token.refreshToken(), 86400 * 30);
    }
}
