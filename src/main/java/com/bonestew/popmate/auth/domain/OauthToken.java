package com.bonestew.popmate.auth.domain;

import java.time.LocalDateTime;
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
public class OauthToken {

    // 이건 나중에
    private Long aaId;
    private LocalDateTime createdAt;
}
