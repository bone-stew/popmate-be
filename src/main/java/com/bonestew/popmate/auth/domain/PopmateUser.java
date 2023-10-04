package com.bonestew.popmate.auth.domain;


import java.util.Collection;
import java.util.List;

import com.bonestew.popmate.user.domain.Role;
import com.bonestew.popmate.user.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

public class PopmateUser implements UserDetails {

    private final Long userId;
    private final String nickname;
    private final List<Role> authorities;

    public PopmateUser(Long userId, String nickname, List<Role> authorities) {
        this.userId = userId;
        this.nickname = nickname;
        this.authorities = authorities;
    }

    static public PopmateUser from(User user) {
        return new PopmateUser(user.getUserId(), user.getNickname(), List.of(user.getRole()));
    }

    @Override
    public Collection<Role> getAuthorities() {
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

    public String getNickname() {return this.nickname; }
}
