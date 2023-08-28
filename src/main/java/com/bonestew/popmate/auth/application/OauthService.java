package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.auth.persistence.OauthDao;
import com.bonestew.popmate.auth.domain.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthDao oauthDao;
    private final KaKaoClient kaKaoClient;
    private final AuthenticationService authenticationService;


    public JwtAuthenticationResponse loginKakaoOauthService(String code) {
        // 카카오 유저 정보 가져오는 곳
        OauthUser oauthUser = kaKaoClient.getUserInfo(code);
        // 유저가 있는지 없는지 확인 로직
        OauthUser user = oauthDao.findCheck(oauthUser.getEmail());
        if(user != null){   // 유저가 있으면
            return authenticationService.signin(user);
        }else{  // 첫 로그인
            return authenticationService.signup(oauthUser);
        }

    }
    public JwtAuthenticationResponse loginGoogleOauthService(OauthUser oauthUser) {
        // 유저가 있는지 없는지 확인 로직
        OauthUser user = oauthDao.findCheck(oauthUser.getEmail());
        if(user != null){   // 유저가 있으면
            return authenticationService.signin(user);
        }else{  // 첫 로그인
            return authenticationService.signup(oauthUser);
        }
    }

}
