package com.bonestew.popmate.auth.presentation.dto;

import com.bonestew.popmate.user.domain.Role;

public record BackOfficeLoginRequest(
    String id,
    String password
) {

}
