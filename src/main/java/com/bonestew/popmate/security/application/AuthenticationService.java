package com.bonestew.popmate.security.application;

import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.security.domain.JwtAuthenticationResponse;

public interface AuthenticationService {

    //가입하기
    JwtAuthenticationResponse signup(OauthUser oauthUser);

    // 로그인하기
    JwtAuthenticationResponse signin(OauthUser oauthUser);
}
