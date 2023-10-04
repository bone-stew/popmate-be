package com.bonestew.popmate.chat.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 123456789L;

    private String roomId;
    private String name;
    private LocalDateTime createdAt;
}
