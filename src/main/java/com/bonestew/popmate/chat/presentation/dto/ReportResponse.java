package com.bonestew.popmate.chat.presentation.dto;

import lombok.Setter;

public record ReportResponse(
        Long reportId,
        Long reporter,
        String chatId,
        String message,
        Integer status,
        Long writer,
        Integer reportCount,
        String writerEmail
) {
}
