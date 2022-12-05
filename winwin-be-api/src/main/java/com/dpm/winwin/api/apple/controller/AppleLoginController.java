package com.dpm.winwin.api.apple.controller;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.LoginCancelException;
import com.dpm.winwin.api.common.response.dto.BaseResponseDto;
import com.dpm.winwin.api.apple.service.AppleLoginService;
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

    private final AppleLoginService appleLoginService;

    @PostMapping(value = "/apple/redirect", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public BaseResponseDto<String> appleRedirect(@RequestBody MultiValueMap<String, String> redirectInfo) throws IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {
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
            String jwtToken = appleLoginService.signUpMember(memberInfo, code);

            log.info("jwtToken : {}", jwtToken);
            return BaseResponseDto.ok(jwtToken);
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
        String token = appleLoginService.singInMember(code);

        return BaseResponseDto.ok(token);
    }
}
