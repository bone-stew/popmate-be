package com.bonestew.popmate.config;

import com.bonestew.popmate.auth.application.JwtService;
import com.bonestew.popmate.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String token =  accessor.getFirstNativeHeader("Authorization");
        log.debug(token);
        if (command == StompCommand.CONNECT) {
            if (!jwtService.validateToken(accessor.getFirstNativeHeader("Authorization")))
                throw new BadRequestException("TokenNotValid");
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
