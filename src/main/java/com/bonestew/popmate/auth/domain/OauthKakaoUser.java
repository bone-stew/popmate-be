package com.bonestew.popmate.auth.domain;

import com.bonestew.popmate.date.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OauthKakaoUser extends BaseTime {

    // 이건 나중에
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String provider;
    private String role;
}
