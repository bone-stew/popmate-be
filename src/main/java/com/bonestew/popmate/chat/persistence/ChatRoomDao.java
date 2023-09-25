package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.domain.ChatRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface ChatRoomDao {

    Optional<ChatRoom> findById(String roomId);
    void insertChatReport(@Param("reporter") Long reporter,@Param("chatId") String chatId);
}
