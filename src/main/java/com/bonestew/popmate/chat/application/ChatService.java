package com.bonestew.popmate.chat.application;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.persistence.ChatRoomDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomDao chatRoomDao;

    public ChatRoom findById(Long roomId) {
        return chatRoomDao.findById(roomId);
    }

}
