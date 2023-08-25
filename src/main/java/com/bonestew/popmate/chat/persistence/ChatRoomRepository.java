package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.application.RedisSubscriber;
import com.bonestew.popmate.chat.domain.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final ChannelTopic channelTopic;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomDao chatRoomDao;

    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
    }

//    public List<ChatRoom> findAllRoom() {
//        return opsHashChatRoom.values(channelTopic.getTopic());
//    }
//
//    public ChatRoom findRoomById(String roomId) {
//        return opsHashChatRoom.get(channelTopic.getTopic(), roomId);
//    }

//    public ChatRoom createChatRoom(ChatRoom chatRoom) {
//        opsHashChatRoom.put(channelTopic.getTopic(), String.valueOf(chatRoom.getRoomId()), chatRoom);
//        return chatRoom;
//    }

    public void enterChatRoom(String roomId) {
        ChatRoom chatRoom = opsHashChatRoom.get(channelTopic.getTopic(), roomId);
        if (chatRoom == null) {
            chatRoom = chatRoomDao.findById(roomId);
            if(chatRoom == null) throw new RuntimeException("채팅방 없음");
            redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic(roomId));
            opsHashChatRoom.put(channelTopic.getTopic(), roomId, chatRoom);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return new ChannelTopic(roomId);
    }
}
