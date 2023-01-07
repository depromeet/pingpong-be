package com.dpm.winwin.api.jwt;


import com.dpm.winwin.api.common.utils.CookieUtil;
import com.dpm.winwin.api.member.dto.PingPongMember;
import com.dpm.winwin.domain.entity.token.ExpiredToken;
import com.dpm.winwin.domain.repository.token.ExpiredTokenRepository;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    private final TokenProvider tokenProvider;
    private final ExpiredTokenRepository expiredTokenRepository;


    // 토큰의 인증 정보를 securityContext에 저장하는 역할을 수행
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String accessToken = getToken(httpServletRequest, CookieUtil.ACCESS_TOKEN);
        String refreshToken = getToken(httpServletRequest, CookieUtil.REFRESH_TOKEN);
        String requestURI = httpServletRequest.getRequestURI();

        if (verifyToken(accessToken)) {
            if (isLogoutToken(accessToken)) {
                log.info("로그아웃 처리된 토큰입니다. : {}", accessToken);
                chain.doFilter(request, response);
                return;
            }

            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri : {}", ((PingPongMember)authentication.getPrincipal()).getMemberId(), requestURI);
            chain.doFilter(request, response);
            return;
        }

        // 액세스 토큰이 만료되어 리프레시 토큰으로 액세스 토큰 재발급
        if (StringUtils.hasText(accessToken) && verifyToken(refreshToken)) {
            log.info("access_token 재발급");
            Claims claims = tokenProvider.getClaims(refreshToken);
            Long memberId = getMemberId(claims);
            log.info("memberId : {}", memberId);
            String memberName = claims.getSubject();
            String newAccessToken = tokenProvider.createToken(memberId, memberName, 1);
            changeAccessToken(httpServletRequest, httpServletResponse, newAccessToken);
            Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri : {}", ((PingPongMember)authentication.getPrincipal()).getMemberId(), requestURI);
            chain.doFilter(request, response);
            return;
        }

        log.info("유효한 JWT 토큰이 없습니다. uri : {}, {}", requestURI, accessToken);
        chain.doFilter(request, response);
    }

    private boolean isLogoutToken(String accessToken) {
        Optional<ExpiredToken> expiredToken = expiredTokenRepository.findById(Objects.requireNonNull(accessToken));
        return expiredToken.isPresent();
    }

    private Long getMemberId(Claims claims) {
        log.info("claims : {}", claims);
        Object id = claims.get("memberId");
        return Long.parseLong(String.valueOf(id));
    }

    private void changeAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        String newAccessToken) {
        CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, CookieUtil.ACCESS_TOKEN);
        CookieUtil.addCookie(httpServletResponse, CookieUtil.ACCESS_TOKEN, newAccessToken, 86400);
    }

    private boolean verifyToken(String jwtToken) {
        return StringUtils.hasText(jwtToken) && tokenProvider.validateToken(jwtToken);
    }

    private String getToken(HttpServletRequest httpServletRequest, String tokenType) {
        Optional<Cookie> optional = CookieUtil.getCookie(httpServletRequest, tokenType);
        if (optional.isPresent()) {
            Cookie cookie = optional.get();
            return cookie.getValue();
        }
        return null;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }
}
