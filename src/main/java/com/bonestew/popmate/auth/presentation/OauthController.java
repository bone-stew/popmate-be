package com.bonestew.popmate.auth.presentation;

import com.bonestew.popmate.auth.application.OauthService;
import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.auth.config.SecurityUtil;
import com.bonestew.popmate.auth.domain.JwtAuthenticationResponse;
import com.bonestew.popmate.auth.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")

public class OauthController {

    private final OauthService oauthService;
    // 여기서는 카카오 쪽
    @PostMapping("/kakao")
    public ResponseEntity<JwtAuthenticationResponse> kakaoOauth(@RequestBody String code) {
        // 여기서 코드는 카카오토큰
        System.out.println(code);
        JwtAuthenticationResponse jwtToken = oauthService.loginKakaoOauthService(code);
        return ResponseEntity.ok(jwtToken);
    }

    // 여기서는 구글쪽
    @PostMapping("/google")
    @ResponseBody
    public ResponseEntity<JwtAuthenticationResponse> googleOauth(@RequestBody OauthUser oauthUser){
        JwtAuthenticationResponse jwtToken = oauthService.loginGoogleOauthService(oauthUser);
        return ResponseEntity.ok(jwtToken);
    }

    // 예시로 JWT토큰 받아서 반환하는 곳 (실제로 쓰는건 아니다.)
    @GetMapping("/resource")
    public ResponseEntity<User> sayHello(@RequestHeader("Authorization") String accessToken) {
        // token 받아서 이렇게 User 정보 가져오면 됩니다
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 또는 밑의 방법으로 유저 정보 가져오면 됩니다.
        System.out.println(SecurityUtil.getUserInfo());

        return ResponseEntity.ok(user);
    }
}
