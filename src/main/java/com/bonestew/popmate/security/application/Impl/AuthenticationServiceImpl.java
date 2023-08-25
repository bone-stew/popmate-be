package com.bonestew.popmate.security.application.Impl;

import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.security.application.AuthenticationService;
import com.bonestew.popmate.security.application.JwtAuthenticationResponse;
import com.bonestew.popmate.security.application.JwtService;
import com.bonestew.popmate.security.domain.User;
import com.bonestew.popmate.security.persistence.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(OauthUser oauthUser) {
        // UserDetail에 넣는 것
        User user = User.builder().email(oauthUser.getEmail()).provider(oauthUser.getProvider())
            .name(oauthUser.getName()).build();
        userDao.register(user);
        String jwtToken = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public JwtAuthenticationResponse signin(OauthUser oauthUser) {
//        authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userDao.findByEmail(oauthUser.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

}
