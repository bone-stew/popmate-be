package com.bonestew.popmate.chat.presentation.dto;

import com.bonestew.popmate.chat.domain.ChatMessage;

import java.util.List;

public record MessagesResponse(
        List<ChatMessage> messages,
        Long userId
) {
    public static MessagesResponse of(List<ChatMessage> messages, Long userId) {
        return new MessagesResponse(messages, userId);
    }
}
