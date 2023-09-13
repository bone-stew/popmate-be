package com.bonestew.popmate.auth.presentation;

import com.bonestew.popmate.auth.application.OauthService;
import com.bonestew.popmate.auth.application.dto.JwtAuthenticationResponse;
import com.bonestew.popmate.auth.presentation.dto.GoogleLoginRequest;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")

public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/kakao")
    public ApiResponse<JwtAuthenticationResponse> loginKaKao(@RequestBody String code) {
        String accessToken = oauthService.loginKakao(code);
        return ApiResponse.success(
            new JwtAuthenticationResponse(accessToken)
        );
    }

    // 여기서는 구글쪽
    @PostMapping("/google")
    public ApiResponse<JwtAuthenticationResponse> loginGoogle(@RequestBody GoogleLoginRequest googleLoginRequest){
        String accessToken = oauthService.loginGoogle(googleLoginRequest);
        return ApiResponse.success(
            new JwtAuthenticationResponse(accessToken)
        );
    }

//    @GetMapping("/resource")
//    public ApiResponse<JwtAuthenticationResponse> fdf(){
//
//        return null;
//    }
}
