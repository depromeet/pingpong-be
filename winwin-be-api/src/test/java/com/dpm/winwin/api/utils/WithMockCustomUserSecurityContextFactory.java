package com.dpm.winwin.api.utils;

import com.dpm.winwin.api.member.dto.PingPongMember;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        final PingPongMember principal = new PingPongMember(Long.parseLong(customUser.memberId()), null);

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);

        securityContext.setAuthentication(auth);
        return securityContext;
    }

}
