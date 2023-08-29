package com.bonestew.popmate.auth.config;

import com.bonestew.popmate.auth.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static User getUserInfo(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }
        return (User) authentication.getPrincipal();
    }
}
