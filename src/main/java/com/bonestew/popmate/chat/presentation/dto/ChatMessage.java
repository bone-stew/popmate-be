package com.bonestew.popmate.chat.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    private String sender;
    private String message;
    private String roomId;
}
