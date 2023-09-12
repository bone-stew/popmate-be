package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query("{roomId: ?0}")
    List<ChatMessage> findChatMessageByRoomIdOrderByCreatedAtAsc(Long roomId);

    @Query("{roomId: ?0}")
    List<ChatMessage> findChatMessageThumbNail(Long roomId, Pageable pageable);
}
