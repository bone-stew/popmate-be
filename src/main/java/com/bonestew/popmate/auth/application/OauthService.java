package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.domain.OauthKakaoUser;
import com.bonestew.popmate.auth.persistence.OauthDao;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    //private final PopupStoreDao popupStoreDao;
    private final OauthDao oauthDao;
    private final KaKaoClient kaKaoClient;

    public OauthKakaoUser loginOauthService(String code) {
        // 카카오 유저 정보 가져오는 곳
        OauthKakaoUser oauthKakaoUser = kaKaoClient.getUserInfo(code);
        // 유저가 있는지 없는지 확인 로직
        Optional<OauthKakaoUser> user = oauthDao.findCheck(oauthKakaoUser.getEmail());
        if(user.isPresent()){   // 유저가 있으면
            return oauthKakaoUser;
        }else{  // 첫 로그인
            oauthDao.insertUser(oauthKakaoUser);
        }

        return oauthKakaoUser;
    }




}
