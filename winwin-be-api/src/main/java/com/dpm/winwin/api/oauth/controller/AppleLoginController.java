package com.dpm.winwin.api.oauth.controller;

import static com.dpm.winwin.api.common.utils.CookieUtil.ACCESS_TOKEN;
import static com.dpm.winwin.api.common.utils.CookieUtil.REFRESH_TOKEN;
import static com.dpm.winwin.api.configuration.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.LoginCancelException;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.common.utils.CookieUtil;
import com.dpm.winwin.api.jwt.TokenResponse;
import com.dpm.winwin.api.oauth.dto.LoginResponse;
import com.dpm.winwin.api.oauth.service.AppleLoginService;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AppleLoginController {

    private final AppleLoginService appleLoginService;

    @Value("${pingpong.url}")
    private String pingpongUri;

    @PostMapping(value = "/apple/redirect", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public BaseResponseDto<LoginResponse> appleRedirect(@RequestBody MultiValueMap<String, String> redirectInfo,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("----> {}", redirectInfo);

        String redirectUri = getRedirectUri(request);

        print(redirectInfo);

        /**
         * 로그인 후 취소한 경우
         */
        if (!ObjectUtils.isEmpty(redirectInfo.get("error"))) {
            log.info("사용자가 로그인을 취소하였습니다.");

            // 임시
            response.sendRedirect(redirectUri);
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
            redirectUri = pingpongUri + "/nickname";
            setResponse(request, response, redirectUri, token);
            return BaseResponseDto.ok(new LoginResponse(token.memberId()));
        }

        String code = redirectInfo.getFirst("code");
        TokenResponse token = appleLoginService.signInMember(code);
        setResponse(request, response, redirectUri, token);
        return BaseResponseDto.ok(new LoginResponse(token.memberId()));
    }

    private void print(MultiValueMap<String, String> redirectInfo) {
        log.info("authorization code :: {}", redirectInfo.get("code"));
        log.info("authorization state :: {}", redirectInfo.get("state"));
        log.info("authorization id_token :: {}", redirectInfo.get("id_token"));
        log.info("authorization user :: {}", redirectInfo.get("user"));
        log.info("error :: {} ", redirectInfo.get("error"));
    }

    private void setResponse(HttpServletRequest request, HttpServletResponse response, String redirectUri,
        TokenResponse token) throws IOException {
        setTokenCookie(request, response, token);
        response.sendRedirect(redirectUri);
    }

    private String getRedirectUri(HttpServletRequest request) {
        Optional<Cookie> optional = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME);
        if (optional.isPresent()) {
            Cookie cookie = optional.get();
            return cookie.getValue();
        }
        return pingpongUri;
    }

    private void setTokenCookie(HttpServletRequest request, HttpServletResponse response, TokenResponse token) {
        CookieUtil.changeCookie(request, response, ACCESS_TOKEN, token.accessToken(), 86400);
        CookieUtil.changeCookie(request, response, REFRESH_TOKEN, token.refreshToken(), 86400 * 30);
    }
}
