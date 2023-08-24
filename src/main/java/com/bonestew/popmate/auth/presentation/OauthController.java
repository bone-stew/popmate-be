package com.bonestew.popmate.auth.presentation;

import com.bonestew.popmate.auth.application.OauthService;
import com.bonestew.popmate.auth.domain.OauthKakaoUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")

public class OauthController {

    private final OauthService oauthService;
    @PostMapping("/oauth/{code}")
    @ResponseBody
    public ApiResponse<OauthKakaoUser> kakaoOauth(@PathVariable String code) {
        // 여기서 코드는 카카오코드(토큰)
        OauthKakaoUser user = oauthService.loginOauthService(code);

        PopupStore popupStore= new PopupStore();
        return ApiResponse.success(user);
    }
}
