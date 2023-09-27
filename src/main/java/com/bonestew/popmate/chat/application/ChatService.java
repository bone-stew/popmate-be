package com.bonestew.popmate.chat.application;

import com.bonestew.popmate.chat.domain.ChatReport;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.exception.ChatRoomNotFoundException;
import com.bonestew.popmate.chat.persistence.ChatMessageRepository;
import com.bonestew.popmate.chat.persistence.ChatRoomDao;
import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.chat.persistence.ChatRoomRepository;
import com.bonestew.popmate.chat.persistence.dto.ReportDto;
import com.bonestew.popmate.chat.presentation.dto.ReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomDao chatRoomDao;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;

    public ChatRoom findRoomById(String roomId) {
        return chatRoomDao.findById(roomId).orElseThrow(ChatRoomNotFoundException::new);
    }

    public ChatRoom enterChatRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.getChatRoom(roomId);
        if (chatRoom == null) {
            chatRoom = chatRoomDao.findById(roomId).orElseThrow(ChatRoomNotFoundException::new);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, getTopic(roomId));
            chatRoomRepository.registerChatRoom(chatRoom);
        }
        return chatRoom;
    }

    public ChannelTopic getTopic(String roomId) {
        return new ChannelTopic(roomId);
    }

    ;

    public List<ChatMessage> loadChatMessagesByRoomId(Long roomId, Pageable pageable) {
        return chatMessageRepository.findChatMessageByRoomId(roomId, pageable);
    }

    public List<ChatMessage> loadChatThumbnail(Long roomId) {
        return chatMessageRepository.findChatMessageThumbNail(roomId, PageRequest.of(0, 10));
    }

    public void reportMessage(ChatMessage chat, Long reporter) throws DuplicateKeyException {
        chatRoomDao.insertChatReport(ReportDto.from(reporter, chat));
    }

    public List<ChatReport> getReports() {
        return chatRoomDao.findReportList();
    }

    public List<ChatReport> getReportsByWriter(Long userId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findChatMessageBySender(userId);
        return chatMessages.stream()
                .map(
                        chat -> new ChatReport(null, null, chat.getId(), chat.getMessage(), null, null, null, null)
                ).toList();
    }
}
