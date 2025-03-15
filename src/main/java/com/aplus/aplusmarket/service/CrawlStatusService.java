package com.aplus.aplusmarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CrawlStatusService {
    private final String CRAWL_STATUS_KEY = "crawl_status:";
    private final RedisTemplate<String, String> redisTemplate;

    public void setCrawling(String keyword) {
        redisTemplate.opsForValue().set(CRAWL_STATUS_KEY + keyword, "IN_PROGRESS", Duration.ofMinutes(10));
    }

    public void setCompleted(String keyword) {
        redisTemplate.opsForValue().set(CRAWL_STATUS_KEY + keyword, "COMPLETED", Duration.ofHours(1));
    }

    public boolean isCrawling(String keyword) {
        String status = redisTemplate.opsForValue().get(CRAWL_STATUS_KEY + keyword);
        return "IN_PROGRESS".equals(status);
    }
}

