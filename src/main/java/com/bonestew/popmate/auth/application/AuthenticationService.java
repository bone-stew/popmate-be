package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.auth.domain.JwtAuthenticationResponse;
import com.bonestew.popmate.auth.domain.User;
import com.bonestew.popmate.auth.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService  {
    private final UserDao userDao;
    private final JwtService jwtService;

    public JwtAuthenticationResponse signup(OauthUser oauthUser) {
        // UserDetail에 넣는 것
        User user = User.builder().email(oauthUser.getEmail()).provider(oauthUser.getProvider())
            .name(oauthUser.getName()).build();
        userDao.register(user);
        String jwtToken = jwtService.generateToken(user.getUserId(), user.getEmail());
        return JwtAuthenticationResponse.builder().token(jwtToken).build();
    }

    public JwtAuthenticationResponse signin(String email) {
//        authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userDao.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String jwt = jwtService.generateToken(user.getUserId(), user.getEmail());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

}
