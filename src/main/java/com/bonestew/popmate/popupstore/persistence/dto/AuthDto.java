package com.bonestew.popmate.popupstore.persistence.dto;

import com.bonestew.popmate.user.domain.Role;

public record AuthDto(
        Long userId,
        Role role
) {
}
