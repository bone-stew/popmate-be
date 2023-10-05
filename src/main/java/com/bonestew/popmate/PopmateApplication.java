package com.bonestew.popmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.bonestew")
public class PopmateApplication {

    public static void main(String[] args) {
        SpringApplication.run(PopmateApplication.class, args);
    }

}
