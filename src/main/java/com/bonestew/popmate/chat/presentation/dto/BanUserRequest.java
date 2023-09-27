package com.bonestew.popmate.chat.presentation.dto;

public record BanUserRequest(
        Long userId,
        Integer type
) {
}
