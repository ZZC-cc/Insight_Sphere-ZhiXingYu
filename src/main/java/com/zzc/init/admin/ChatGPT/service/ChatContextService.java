package com.zzc.init.admin.ChatGPT.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChatContextService {
    
    private RedisTemplate<String, List<String>> redisTemplate;

    private static final String CONTEXT_KEY_PREFIX = "chat:context:";

    // 缓存上下文
    public void cacheChatContext(Long sessionId, List<String> messages) {
        String key = CONTEXT_KEY_PREFIX + sessionId;
        redisTemplate.opsForValue().set(key, messages, 10, TimeUnit.MINUTES);
    }

    // 获取缓存的上下文
    public List<String> getCachedChatContext(Long sessionId) {
        String key = CONTEXT_KEY_PREFIX + sessionId;
        return redisTemplate.opsForValue().get(key);
    }
}
