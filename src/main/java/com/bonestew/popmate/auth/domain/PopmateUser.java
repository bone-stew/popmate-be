package com.bonestew.popmate.auth.domain;


import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class PopmateUser implements UserDetails {

    private final Long userId;
    private final String name;
    private final List<GrantedAuthority> authorities;

    public PopmateUser(Long userId, String name, List<GrantedAuthority> authorities) {
        this.userId = userId;
        this.name = name;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getName() {return this.name; }
}
