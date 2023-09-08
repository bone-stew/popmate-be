package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.domain.SocialProvider;
import com.bonestew.popmate.auth.domain.User;
import com.bonestew.popmate.auth.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService  {
    private final UserDao userDao;
    private final JwtService jwtService;

    public String signup(String name, String email, SocialProvider provider) {
        // UserDetail에 넣는 것
        User user = User.builder()
            .email(email)
            .provider(provider)
            .name(name)
            .build();
        userDao.register(user);
        return jwtService.generateToken(user.getUserId(), user.getEmail());
    }

    public String signin(String email) {
//        authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userDao.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        return jwtService.generateToken(user.getUserId(), user.getEmail());
    }

}
