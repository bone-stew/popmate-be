package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.auth.persistence.OauthDao;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthDao oauthDao;
    private final KaKaoClient kaKaoClient;

    public OauthUser loginKakaoOauthService(String code) {
        // 카카오 유저 정보 가져오는 곳
        OauthUser oauthUser = kaKaoClient.getUserInfo(code);
        // 유저가 있는지 없는지 확인 로직
        Optional<OauthUser> user = oauthDao.findCheck(oauthUser.getEmail());
        if(user.isPresent()){   // 유저가 있으면
            return oauthUser;
        }else{  // 첫 로그인
            oauthDao.insertUser(oauthUser);
        }

        return oauthUser;
    }


    public OauthUser loginGoogleOauthService(OauthUser oauthUser) {
        // 유저가 있는지 없는지 확인 로직
        Optional<OauthUser> user = oauthDao.findCheck(oauthUser.getEmail());
        if(user.isPresent()){   // 유저가 있으면
            return oauthUser;
        }else{  // 첫 로그인
            oauthDao.insertUser(oauthUser);
        }

        return oauthUser;
    }
}
