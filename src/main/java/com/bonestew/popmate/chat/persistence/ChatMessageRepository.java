package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query("{roomId: ?0}")
    List<ChatMessage> findChatMessageByRoomId(Long roomId, Pageable pageable);

    @Query("{roomId: ?0}")
    List<ChatMessage> findChatMessageThumbNail(Long roomId, Pageable pageable);

    @Query("{id: ?0}")
    Optional<ChatMessage> findChatMessageById(String id);

    @Query("{sender: ?0}")
    List<ChatMessage> findChatMessageBySender(Long sender);
}
