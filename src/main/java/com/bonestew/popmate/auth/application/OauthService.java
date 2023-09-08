package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.application.dto.UserInformationDto;
import com.bonestew.popmate.auth.domain.SocialProvider;
import com.bonestew.popmate.auth.domain.User;
import com.bonestew.popmate.auth.persistence.OauthDao;
import com.bonestew.popmate.auth.presentation.dto.GoogleLoginRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthDao oauthDao;
    private final KaKaoClient kaKaoClient;
    private final AuthenticationService authenticationService;

    public String loginKakao(String code) {
        UserInformationDto userInformation = kaKaoClient.getUserInformation(code);
        User user = oauthDao.selectByEmail(userInformation.email());
        if (user != null) {
            return authenticationService.signin(user.getEmail());
        }
        return authenticationService.signup(userInformation.name(), userInformation.email(), SocialProvider.KAKAO);
    }

    public String loginGoogle(GoogleLoginRequest request) {
        User user = oauthDao.selectByEmail(request.email());
        if (user != null) {
            return authenticationService.signin(user.getEmail());
        }
        return authenticationService.signup(request.name(), request.email(), SocialProvider.GOOGLE);
    }

}
