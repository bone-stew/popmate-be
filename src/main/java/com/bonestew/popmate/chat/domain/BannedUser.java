package com.bonestew.popmate.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannedUser {

    private Long bannedId;
    private Long userId;
    private LocalDateTime expiredDate;
}
