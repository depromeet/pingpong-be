package com.dpm.winwin.api.utils;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String memberId() default "1";
}
