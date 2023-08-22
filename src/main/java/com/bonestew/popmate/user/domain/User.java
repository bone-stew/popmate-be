package com.bonestew.popmate.user.domain;

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
public class User extends BaseEntity {

    private Long userId;
    private String email;
    private String password;
    private String provider;
    private String role;
}
