package com.bonestew.popmate.chat.presentation;

import com.bonestew.popmate.chat.application.ChatService;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.presentation.dto.ChatMessage;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatRoomService;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/message")
    public void greeting(ChatMessage message) {
        sendingOperations.convertAndSend("/topic/" + message.getRoomId(), message);
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse<ChatRoom> roomInfo(@PathVariable Long roomId) {
        return ApiResponse.success(chatRoomService.findById(roomId));
    }

}
