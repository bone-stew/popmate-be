package com.bonestew.popmate.utils;

import com.bonestew.popmate.auth.domain.PopmateUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        final PopmateUser principal = new PopmateUser(Long.parseLong(customUser.userId()), null ,null);

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);

        securityContext.setAuthentication(auth);
        return securityContext;
    }
}
