package com.bonestew.popmate.security.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private Long userId;
    private String email;
    private String password;
    private String provider;
    private String role;
    private String name;
    private LocalDateTime createdAt;


    // 사용자에게 부여된 권한을 반환합니다. (ENUM 할 때 적용되는 것)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 사용자를 인증하는 데 사용되는 사용자 이름을 반환합니다.
    // 우리는 이메일로 할 것이다.
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자의 계정이 만료되었는지 여부를 나타냅니다.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    // 사용자가 잠겨 있는지 또는 잠금 해제되어 있는지를 나타냅니다.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 사용자의 자격 증명(비밀번호)이 만료되었는지 여부를 나타냅니다.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용자가 활성화되었는지 또는 비활성화되었는지 여부를 나타냅니다.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
