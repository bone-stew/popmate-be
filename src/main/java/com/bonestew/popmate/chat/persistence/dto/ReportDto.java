package com.bonestew.popmate.chat.persistence.dto;

import com.bonestew.popmate.chat.domain.ChatMessage;

public record ReportDto(
        Long reporter,
        String id,
        String message,
        Long sender
) {
    public static ReportDto from (Long reporter, ChatMessage chatMessage) {
        return new ReportDto(reporter, chatMessage.getId(), chatMessage.getMessage(), chatMessage.getSender());
    }
}
