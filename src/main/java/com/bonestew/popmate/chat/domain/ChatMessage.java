package com.bonestew.popmate.chat.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document("chatMessages")
public class ChatMessage {

    @Id
    private String id;

    private String sender;
    private String message;
    private String roomId;
//    private LocalDateTime createdAt = LocalDateTime.now();;
}
