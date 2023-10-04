package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.user.domain.SocialProvider;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.user.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService  {
    private final UserDao userDao;
    private final JwtService jwtService;
    private final RandomNickNameService randomNickNameService;

    public String signup(String name, String email, SocialProvider provider) {
        // UserDetail에 넣는 것
        User user = User.builder()
            .email(email)
            .provider(provider)
            .name(name)
            .nickname(randomNickNameService.generate())
            .build();
        userDao.register(user);
        user.setUserId(userDao.findLoginId(user.getEmail()));
        return jwtService.generateToken(user);
    }

    public String signin(String email) {
//        authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userDao.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        return jwtService.generateToken(user);
    }

}
