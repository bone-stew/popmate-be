package com.bonestew.popmate.chat.domain;

import com.bonestew.popmate.date.BaseTime;
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
public class ChatRoom extends BaseTime implements Serializable {

    @Serial
    private static final long serialVersionUID = 123456789L;

    private String roomId;
    private String name;

    public static ChatRoom create(String roomId,String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = roomId;
        chatRoom.name = name;
        return chatRoom;
    }
}
