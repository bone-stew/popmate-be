package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.domain.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatRoomDao {

    ChatRoom findById(Long id);
}
