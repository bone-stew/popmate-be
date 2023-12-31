package com.bonestew.popmate.auth.application;

import com.bonestew.popmate.auth.application.dto.FreeTokenRequest;
import com.bonestew.popmate.auth.application.dto.UserInformationDto;
import com.bonestew.popmate.auth.exception.BackOfficeUserNotFoundException;
import com.bonestew.popmate.auth.presentation.dto.BackOfficeLoginRequest;
import com.bonestew.popmate.user.domain.SocialProvider;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.auth.presentation.dto.GoogleLoginRequest;
import com.bonestew.popmate.user.exception.UserNotFoundException;
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
    private final JwtService jwtService;

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

    public String loginBackOffice(BackOfficeLoginRequest backOfficeLoginRequest) {
        Optional<User> user = userDao.findBackOfficeUser(backOfficeLoginRequest.id(),backOfficeLoginRequest.password());
        if(user.isPresent()){
            return authenticationService.signin(user.get().getEmail());
        }else{
            throw new BackOfficeUserNotFoundException("유효하지 않은 아이디와 비밀번호입니다.");
        }
    }

    public String getFreeToken(FreeTokenRequest freeTokenRequest) {
        User user = userDao.findById(freeTokenRequest.userId())
            .orElseThrow(() -> new UserNotFoundException(freeTokenRequest.userId()));
        return jwtService.generateToken(user);
    }
}
