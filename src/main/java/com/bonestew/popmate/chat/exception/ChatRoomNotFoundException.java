package com.bonestew.popmate.chat.exception;

import com.bonestew.popmate.exception.NotFoundException;

public class ChatRoomNotFoundException extends NotFoundException {
    public ChatRoomNotFoundException() {
        super("채팅방이 존재하지 않습니다.");
    }
}
