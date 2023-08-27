package com.bonestew.popmate.chat.presentation;

import com.bonestew.popmate.chat.application.ChatService;
import com.bonestew.popmate.chat.application.RedisPublisher;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
        redisPublisher.publish(chatService.getTopic(message.getRoomId()), message);
    }

    /**
     * 채팅방 입장 API
     * @param roomId 채팅방 ID
     */
    @GetMapping("/enter/{roomId}")
    public ApiResponse<String> enter(@PathVariable String roomId) {
        chatService.enterChatRoom(roomId);
        return ApiResponse.success("입장 성공");
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
    public ApiResponse<List<ChatMessage>> messages(@PathVariable String roomId) {
        log.debug("{}번 채팅방 메세지 조회 API 호출", roomId);
        return ApiResponse.success(chatService.loadChatMessagesByRoomId(roomId));
    }
}