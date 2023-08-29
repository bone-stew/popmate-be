package com.bonestew.popmate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootTest
@EnableMongoRepositories
@ExtendWith(TestContainerConfig.class)
class PopmateApplicationTests {

    @Test
    void contextLoads() {}

}
