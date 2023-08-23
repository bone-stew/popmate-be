package com.bonestew.popmate.chat.presentation;

import com.bonestew.popmate.chat.application.ChatService;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatRoomService;

    @MessageMapping("/message/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage greeting(@DestinationVariable Long roomId, ChatMessage message) {
        log.debug("RoomID: {}, Sender: {}, message: {}", roomId, message.getSender(), message.getMessage());
        message.setCreatedAt(LocalDateTime.now());
        chatRoomService.saveMessage(message);
        return message;
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse<ChatRoom> roomInfo(@PathVariable Long roomId) {
        return ApiResponse.success(chatRoomService.findById(roomId));
    }

    @GetMapping("/room/messages/{roomId}")
    public ApiResponse<List<ChatMessage>> messages(@PathVariable Long roomId) {
        return ApiResponse.success(chatRoomService.loadChatMessagesByRoomId(roomId));
    }

}
