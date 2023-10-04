package com.bonestew.popmate.auth.presentation;

import com.bonestew.popmate.auth.application.OauthService;
import com.bonestew.popmate.auth.application.dto.FreeTokenRequest;
import com.bonestew.popmate.auth.application.dto.JwtAuthenticationResponse;
import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.auth.presentation.dto.BackOfficeLoginRequest;
import com.bonestew.popmate.auth.presentation.dto.GoogleLoginRequest;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.exception.BadRequestException;
import com.bonestew.popmate.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@Slf4j
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

    // 관리자 로그인
    @PostMapping("/office")
    public ApiResponse<JwtAuthenticationResponse> loginOffice(@RequestBody BackOfficeLoginRequest backOfficeLoginRequest){
        log.info(backOfficeLoginRequest.toString());
        String accessToken = oauthService.loginBackOffice(backOfficeLoginRequest);
        return ApiResponse.success(
            new JwtAuthenticationResponse(accessToken)
        );
    }

    /**
     * 토큰을 개발용으로 편하게 가져올 수 있는 API
     *
     * @return 토큰
     */
    @PostMapping("/free")
    public ApiResponse<JwtAuthenticationResponse> createFreeToken(@RequestBody FreeTokenRequest freeTokenRequest) {
        String accessToken = oauthService.getFreeToken(freeTokenRequest);
        return ApiResponse.success(
            new JwtAuthenticationResponse(accessToken)
        );
    }

    @GetMapping("/me")
    public ApiResponse<PopmateUser> me(@AuthenticationPrincipal PopmateUser user) {
        if(user == null) throw new BadRequestException("유효한 토큰이 아닙니다.");
        return ApiResponse.success(user);
    }
}
