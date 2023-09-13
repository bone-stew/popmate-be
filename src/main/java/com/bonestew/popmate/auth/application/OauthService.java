package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.application.dto.UserInformationDto;
import com.bonestew.popmate.user.domain.SocialProvider;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.auth.presentation.dto.GoogleLoginRequest;
import com.bonestew.popmate.user.persistence.UserDao;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserDao userDao;
    private final KaKaoClient kaKaoClient;
    private final AuthenticationService authenticationService;

    public String loginKakao(String code) {
        UserInformationDto userInformation = kaKaoClient.getUserInformation(code);
        Optional<User> user = userDao.findByEmail(userInformation.email());
        if (user.isPresent()) {
            return authenticationService.signin(user.get().getEmail());
        }
        return authenticationService.signup(userInformation.name(), userInformation.email(), SocialProvider.KAKAO);
    }

    public String loginGoogle(GoogleLoginRequest request) {
        Optional<User> user = userDao.findByEmail(request.email());
        if (user.isPresent()) {
            return authenticationService.signin(user.get().getEmail());
        }
        return authenticationService.signup(request.name(), request.email(), SocialProvider.GOOGLE);
    }

}
