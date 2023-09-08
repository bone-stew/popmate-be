package com.bonestew.popmate.auth.application.dto;

import com.bonestew.popmate.auth.domain.KakaoUserResponse;

public record UserInformationDto(
    String name,
    String email
) {

}
