package com.spring.springboot.UrlShortener.configTests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    @Disabled
    void checkRedisConnection(){
        String name = redisTemplate.opsForValue().get("name");
        redisTemplate.opsForValue().set("wom", "hot");
        Long urlCounter = redisTemplate.opsForValue().increment("url_counter");
        int a = 5;
    }
}
