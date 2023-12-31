package com.bonestew.popmate.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
//    @DependsOn("embeddedRedisConfig")
    public RedissonClient redissonClient() {
        final String address = "redis://" + redisHost + ":" + redisPort;
        final Config config = new Config();
        config.useSingleServer().setAddress(address);
        return Redisson.create(config);
    }
}
