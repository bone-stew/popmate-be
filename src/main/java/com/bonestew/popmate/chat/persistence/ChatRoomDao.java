package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.domain.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ChatRoomDao {

    Optional<ChatRoom> findById(String roomId);
}
