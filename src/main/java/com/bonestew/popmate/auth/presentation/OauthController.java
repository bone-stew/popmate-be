package com.bonestew.popmate.auth.presentation;

import com.bonestew.popmate.auth.application.OauthService;
import com.bonestew.popmate.auth.domain.OauthUser;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")

public class OauthController {

    private final OauthService oauthService;

    // 여기서는 카카오 쪽
    @PostMapping("/oauth/{code}")
    @ResponseBody
    public ApiResponse<OauthUser> kakaoOauth(@PathVariable String code) {
        // 여기서 코드는 카카오코드(토큰)
        OauthUser user = oauthService.loginKakaoOauthService(code);
        return ApiResponse.success(user);
    }

    // 여기서는 구글쪽
    @PostMapping("/oauth/google")
    @ResponseBody
    public ApiResponse<OauthUser> googleOauth(@RequestBody OauthUser oauthUser){
        OauthUser user = oauthService.loginGoogleOauthService(oauthUser);

        return ApiResponse.success(user);
    }
}
