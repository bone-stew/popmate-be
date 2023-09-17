package com.bonestew.popmate.user.preservation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.user.application.UserService;
import com.bonestew.popmate.user.application.dto.UserInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 나의 정보 조회
     *
     * @return 회원 정보
     */
    @GetMapping("/me")
    public ApiResponse<UserInformationResponse> getMyInformation(@AuthenticationPrincipal PopmateUser popmateUser) {
        return ApiResponse.success(
            new UserInformationResponse(
                userService.getUserById(popmateUser.getUserId()).getName()
            )
        );
    }
}
