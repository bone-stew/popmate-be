package com.bonestew.popmate.chat.presentation.dto;

import com.bonestew.popmate.chat.domain.ChatMessage;

import java.util.List;

public record MessagesResponse(
        List<ChatMessage> messages,
        CurrUser currUser
) {
    public static MessagesResponse of(List<ChatMessage> messages, Long userId, String name) {
        return new MessagesResponse(messages, new CurrUser(userId, name));
    }

    public static MessagesResponse of(List<ChatMessage> messages) {
        return new MessagesResponse(messages, null);
    }
}
