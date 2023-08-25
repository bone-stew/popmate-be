package com.bonestew.popmate.chat.application;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.persistence.ChatMessageRepository;
import com.bonestew.popmate.chat.persistence.ChatRoomDao;
import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.chat.persistence.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomDao chatRoomDao;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findRoomById(String roomId) {
        return chatRoomDao.findById(roomId);
    }

    public void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> loadChatMessagesByRoomId(String roomId) {
        return chatMessageRepository.findChatMessageByRoomId(roomId);
    }
}
