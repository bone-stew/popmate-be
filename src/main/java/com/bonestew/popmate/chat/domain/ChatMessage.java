package com.bonestew.popmate.chat.domain;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document("chatMessages")
public class ChatMessage {

    @Id
    private String id;

    private String sender;
    private String message;
    private String roomId;
    private LocalDateTime createdAt;

    @PostConstruct
    void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
