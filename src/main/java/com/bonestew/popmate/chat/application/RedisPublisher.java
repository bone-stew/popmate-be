package com.bonestew.popmate.chat.application;

import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.chat.persistence.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public void publish(ChannelTopic topic, ChatMessage message) {
        chatMessageRepository.save(message);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
