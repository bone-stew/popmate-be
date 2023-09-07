package com.bonestew.popmate.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.maxHeap}")
    private String maxHeap;

    private RedisServer redisServer;

    @PostConstruct
    public void init() {

        final String configLine = "maxmemory " + maxHeap;
        redisServer = RedisServer.builder()
            .port(redisPort)
            .setting(configLine).build();
        redisServer.start();
    }

    @PreDestroy
    public void destroy() {
        if(redisServer != null) {
            redisServer.stop();
        }
    }
}
