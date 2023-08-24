package com.bonestew.popmate.auth.domain;

import lombok.Data;

@Data
public class KakaoUserResponse {
    private long id;
    private String connected_at;
    KakaoAccount kakao_account;

    @Data
    public class KakaoAccount {
        String email;
        Profile profile;

        @Data
        public class Profile{
            String nickname;
        }
    }

    public OauthKakaoUser toUser() {
        OauthKakaoUser oauthKakaoUser = new OauthKakaoUser();
        oauthKakaoUser.setName(this.kakao_account.getProfile().getNickname());
        oauthKakaoUser.setEmail(this.kakao_account.getEmail());
        oauthKakaoUser.setProvider("Kakao");
        return oauthKakaoUser;
    }
}
