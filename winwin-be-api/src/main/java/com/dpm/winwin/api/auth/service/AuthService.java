package com.dpm.winwin.api.auth.service;

import static com.dpm.winwin.api.common.utils.CookieUtil.ACCESS_TOKEN;
import static com.dpm.winwin.api.common.utils.CookieUtil.REFRESH_TOKEN;

import com.dpm.winwin.api.common.utils.CookieUtil;
import com.dpm.winwin.domain.entity.token.ExpiredToken;
import com.dpm.winwin.domain.repository.token.ExpiredTokenRepository;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ExpiredTokenRepository expiredTokenRepository;
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        saveExpiredToken(request);

        CookieUtil.addCookie(response, ACCESS_TOKEN, "", 0);
        CookieUtil.addCookie(response, REFRESH_TOKEN, "", 0);
    }

    private void saveExpiredToken(HttpServletRequest request) {
        Cookie cookie = CookieUtil.getCookie(request, ACCESS_TOKEN).orElseThrow();
        String jwt = cookie.getValue();
        ExpiredToken expiredToken = new ExpiredToken(jwt);
        expiredTokenRepository.save(expiredToken);
    }

}
