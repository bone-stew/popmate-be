package com.bonestew.popmate.chat.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.chat.application.ChatService;
import com.bonestew.popmate.chat.application.RedisPublisher;
import com.bonestew.popmate.chat.domain.ChatReport;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.chat.presentation.dto.MessagesResponse;
import com.bonestew.popmate.chat.presentation.dto.ReportResponse;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
  
    /**
     * 메세지 전송 API
     * @param message sender, roomId, message
     */
    @MessageMapping("/message")
    public void message(ChatMessage message) {
        log.debug("메세지 전송: {}", message);
        redisPublisher.publish(chatService.getTopic(String.valueOf(message.getRoomId())), message);
    }

    /**
     * 채팅방 입장 API
     * @param roomId 채팅방 ID
     */
    @GetMapping("/enter/{roomId}")
    public ApiResponse<PopmateUser> enter(@AuthenticationPrincipal PopmateUser userPrincipal ,@PathVariable String roomId) {
        chatService.enterChatRoom(roomId);
        return ApiResponse.success(userPrincipal);
    }

    /**
     * 채팅방 조회 API
     * @param roomId 채팅방 ID
     * @return 조회된 채팅방
     */
    @GetMapping("/room/{roomId}")
    public ApiResponse<ChatRoom> room(@PathVariable String roomId) {
//        return ApiResponse.success(chatRoomService.findById(roomId));
        log.debug("{}번 채팅방 조회 API", roomId);
        return ApiResponse.success(chatService.findRoomById(roomId));
    }

    /**
     * 채팅방 메세지 목록 호출 api
     * @param roomId 채팅방 메세지 목록 호출
     * @return 채팅 메세지 리스트
     */
    @GetMapping("/room/messages/{roomId}")
    public ApiResponse<MessagesResponse> messages(Pageable pageable , @PathVariable Long roomId) {
        log.debug("{}번 채팅방 메세지 조회 API 호출", roomId);
        List<ChatMessage> res = chatService.loadChatMessagesByRoomId(roomId, pageable);
        return ApiResponse.success(MessagesResponse.of(res));
    }

    @GetMapping("/thumbnail/{roomId}")
    public  ApiResponse<MessagesResponse> thumbnail(@PathVariable Long roomId) {
        return ApiResponse.success(MessagesResponse.of(chatService.loadChatThumbnail(roomId)));
    }

    @PostMapping("/report")
    public ApiResponse<String> report(@AuthenticationPrincipal PopmateUser principal, @RequestBody ChatMessage chat) {
        try {
            chatService.reportMessage(chat, principal.getUserId());
            return ApiResponse.success("신고가 접수되었습니다.");
        } catch (DuplicateKeyException e) {
            return ApiResponse.success("이미 신고가 접수되었습니다.");
        }
    }

    @GetMapping("/report")
    public ApiResponse<List<ChatReport>> reportList() {
        return ApiResponse.success(chatService.getReports());
    }

    @GetMapping("/report/{userId}")
    public ApiResponse<List<ChatReport>> reportListByWriter(@PathVariable Long userId) {
        return ApiResponse.success(chatService.getReportsByWriter(userId));
    }
}
