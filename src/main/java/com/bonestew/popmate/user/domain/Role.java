package com.bonestew.popmate.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_MANAGER,
    ROLE_STAFF;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
