package com.dpm.winwin.api.jwt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 유효한 자격 증명을 제공하지 않고 접근하려 할 때, 401 UnAuthorized 에러를 리턴
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        log.info("인증되지 않은 요청입니다.");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        /**
         * TODO : 인증과정에서 에러 발생 시 json으로 401
         */
    }
}
