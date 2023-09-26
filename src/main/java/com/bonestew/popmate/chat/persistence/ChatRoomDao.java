package com.bonestew.popmate.chat.persistence;

import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.chat.domain.ChatReport;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.persistence.dto.ReportDto;
import com.bonestew.popmate.chat.presentation.dto.ReportResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatRoomDao {

    Optional<ChatRoom> findById(String roomId);
    void insertChatReport(ReportDto report);
    List<ChatReport> findReportList();

    List<ChatReport> findReportListByWriterId(Long userId);
}
