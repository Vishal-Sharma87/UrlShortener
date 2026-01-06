package com.spring.springboot.UrlShortener.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springboot.UrlShortener.dto.RedisDtos.LinkDocForCacheDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public Long getUrlCounter() {
        return redisTemplate.opsForValue().increment("url_counter");
    }

    //    Getter
    public LinkDocForCacheDto get(String id) throws JsonProcessingException {
        String storedJsonContent = redisTemplate.opsForValue().get(id);
        if (storedJsonContent == null) {
            return null;
        }
        return objectMapper.readValue(storedJsonContent, LinkDocForCacheDto.class);
    }

    //    setter
    public void set(String id, LinkDocForCacheDto document, int ttl, TimeUnit timeUnit) throws JsonProcessingException {
        String jsonConvertedStringContent = objectMapper.writeValueAsString(document);

        redisTemplate.opsForValue().set(id, jsonConvertedStringContent, ttl, timeUnit);
    }
}
