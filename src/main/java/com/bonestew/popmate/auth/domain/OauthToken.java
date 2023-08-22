package com.bonestew.popmate.auth.domain;

import com.bonestew.popmate.date.BaseEntity;
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
public class OauthToken extends BaseEntity {

    // 이건 나중에
    private Long aaId;
}
