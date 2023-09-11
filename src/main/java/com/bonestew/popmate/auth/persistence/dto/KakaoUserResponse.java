package com.bonestew.popmate.auth.persistence.dto;

public record KakaoUserResponse(
    Long id,
    String connected_at,
    KakaoAccount kakao_account
) {

    public record KakaoAccount(

        String email,
        Profile profile
    ) {

        public record Profile(

            String nickname
        ) {

        }
    }

}
