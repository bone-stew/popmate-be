package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.domain.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
    }

    public ChatRoom getChatRoom(String roomId) {
        return opsHashChatRoom.get(channelTopic.getTopic(), roomId);
    }

    public void registerChatRoom(ChatRoom chatRoom) {
        opsHashChatRoom.put(channelTopic.getTopic(), chatRoom.getRoomId(), chatRoom);
    }
}
