package com.bonestew.popmate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //enable simple memory-based message broker to carry the greeting messages back to the client on destinations prefixed with /topic
        config.enableSimpleBroker("/sub");
        // prefix for messages that are bound for methods annotated with @MessageMapping
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //registers the /gs-guide-websocket endpoint for websocket connections.
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*");
    }
}
