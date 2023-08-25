package com.bonestew.popmate.auth.presentation;

import com.bonestew.popmate.auth.application.OauthService;
import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.security.application.AuthenticationService;
import com.bonestew.popmate.security.application.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")

public class OauthController {

    private final OauthService oauthService;
    private final AuthenticationService authenticationService;
    // 여기서는 카카오 쪽
    @PostMapping("/oauth/{code}")
    @ResponseBody
    public ResponseEntity<JwtAuthenticationResponse> kakaoOauth(@PathVariable String code) {
        // 여기서 코드는 카카오토큰
        JwtAuthenticationResponse jwtToken = oauthService.loginKakaoOauthService(code);
        return ResponseEntity.ok(jwtToken);
    }

    // 여기서는 구글쪽
    @PostMapping("/oauth/google")
    @ResponseBody
    public ResponseEntity<JwtAuthenticationResponse> googleOauth(@RequestBody OauthUser oauthUser){
        JwtAuthenticationResponse jwtToken = oauthService.loginGoogleOauthService(oauthUser);

        return ResponseEntity.ok(jwtToken);
    }
}
