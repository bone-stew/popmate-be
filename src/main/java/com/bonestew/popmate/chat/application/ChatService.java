package com.bonestew.popmate.chat.application;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.persistence.ChatMessageRepository;
import com.bonestew.popmate.chat.persistence.ChatRoomDao;
import com.bonestew.popmate.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomDao chatRoomDao;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom findById(Long roomId) {
        return chatRoomDao.findById(roomId);
    }

    public void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> loadChatMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findChatMessageByRoomId(roomId);
    }
}
