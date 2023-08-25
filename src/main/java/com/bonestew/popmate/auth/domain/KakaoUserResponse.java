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

    public OauthUser toUser() {
        OauthUser oauthUser = new OauthUser();
        oauthUser.setName(this.kakao_account.getProfile().getNickname());
        oauthUser.setEmail(this.kakao_account.getEmail());
        oauthUser.setProvider("Kakao");
        return oauthUser;
    }
}
