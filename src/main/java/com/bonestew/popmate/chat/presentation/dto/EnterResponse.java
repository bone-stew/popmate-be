package com.bonestew.popmate.chat.presentation.dto;

import java.time.LocalDateTime;

public record EnterResponse(Boolean denied, LocalDateTime until) {
}
