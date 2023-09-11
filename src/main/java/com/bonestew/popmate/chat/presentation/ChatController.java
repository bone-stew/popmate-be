package com.bonestew.popmate.chat.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.chat.application.ChatService;
import com.bonestew.popmate.chat.application.RedisPublisher;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.chat.presentation.dto.MessagesResponse;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private Long userId = 2L;
    private String userName ="김우원";
    /**
     * 메세지 전송 API
     * @param message sender, roomId, message
     */
    @MessageMapping("/message")
    public void message(ChatMessage message) {
        log.debug("메세지 전송: {}", message);
        message.setSender(userId);
        message.setName(userName);
        redisPublisher.publish(chatService.getTopic(String.valueOf(message.getRoomId())), message);
    }

    /**
     * 채팅방 입장 API
     * @param roomId 채팅방 ID
     */
    @GetMapping("/enter/{roomId}")
    public ApiResponse<ChatRoom> enter(@PathVariable String roomId) {
        return ApiResponse.success(chatService.enterChatRoom(roomId));
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
    public ApiResponse<MessagesResponse> messages(@AuthenticationPrincipal PopmateUser user, @PathVariable Long roomId) {
        log.debug("{}번 채팅방 메세지 조회 API 호출", roomId);
        List<ChatMessage> res = chatService.loadChatMessagesByRoomId(roomId);
        log.debug("채팅방 메세지 조회 API 호출 결과 {}", res);
        return ApiResponse.success(MessagesResponse.of(res, user.getUserId(), user.getName()));
    }
}
