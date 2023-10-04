package com.bonestew.popmate.chat.presentation.dto;

import com.bonestew.popmate.chat.domain.ChatMessage;

import java.util.List;

public record MessagesResponse(
        List<ChatMessage> messages
) {
    public static MessagesResponse of(List<ChatMessage> messages) {
        return new MessagesResponse(messages);
    }
}
