package com.bonestew.popmate.security.application.Impl;

import com.bonestew.popmate.security.application.UserService;
import com.bonestew.popmate.security.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            // 사용자 이름을 기준으로 사용자를 찾습니다. (이 프로젝트는 이메일로 한다)
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userDao.findByEmail(username)
                    .orElseThrow(()->new UsernameNotFoundException("User not found"));
            }
        };
    }
}
