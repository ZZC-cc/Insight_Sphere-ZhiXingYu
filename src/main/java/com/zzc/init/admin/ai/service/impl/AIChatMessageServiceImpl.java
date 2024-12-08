package com.zzc.init.admin.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.ai.model.dto.AIChatRequest;
import com.zzc.init.admin.ai.model.dto.AIChatResponse;
import com.zzc.init.admin.ai.model.entity.AIChatMessage;
import com.zzc.init.admin.ai.model.entity.AIChatSession;
import com.zzc.init.admin.ai.model.entity.AIModel;
import com.zzc.init.admin.ai.service.AIChatMessageService;
import com.zzc.init.admin.ai.service.AIPlatformStrategy;
import com.zzc.init.admin.ai.service.BaseAIService;
import com.zzc.init.mapper.AIChatMessageMapper;
import com.zzc.init.mapper.AIChatSessionMapper;
import com.zzc.init.mapper.AIModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AIChatMessageServiceImpl extends ServiceImpl<AIChatMessageMapper, AIChatMessage> implements AIChatMessageService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AIPlatformStrategy aiPlatformStrategy;

    @Resource
    private BaseAIService baseAIService;

    @Autowired
    private AIChatSessionMapper aiChatSessionMapper;

    @Autowired
    private AIModelMapper aiModelMapper;

    private static final String CHAT_CONTEXT_PREFIX = "chat_context:"; // Redis键前缀

    @Override
    public AIChatResponse sendMessage(AIChatRequest request, Long sessionId) {

        String userContent = request.getMessages().stream()
                .filter(message -> "user".equals(message.getRole())) // 筛选用户消息
                .map(AIChatRequest.Message::getContent) // 获取内容
                .reduce((first, second) -> second) // 取最后一条
                .orElse(""); // 如果没有用户消息则为空

        if (userContent.isEmpty()) {
            throw new IllegalArgumentException("用户发送的内容不能为空！");
        }
        QueryWrapper<AIModel> query = new QueryWrapper<>();
        query.eq("model_name", request.getModel());
        AIModel aiModel = aiModelMapper.selectOne(query);

        QueryWrapper<AIChatSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", sessionId);
        AIChatSession session = aiChatSessionMapper.selectOne(queryWrapper);
        if (session.getSummary() == null || session.getSession_name() == null || session.getSummary() == "" || session.getSession_name() == "") {
            this.generateTitleAndSummary(sessionId, userContent, aiModel.getPlatform_id(), request.getModel());
        }

        // 创建聊天消息对象
        AIChatMessage message = new AIChatMessage();
        message.setSession_id(sessionId);
        message.setRole("user");
        message.setContent(userContent);
        message.setCreated_time(LocalDateTime.now());
        this.baseMapper.insert(message);

        AIChatResponse aiChatResponse = baseAIService.sendMessage(request);

        AIChatMessage replyMessage = new AIChatMessage();
        replyMessage.setSession_id(sessionId);
        replyMessage.setRole("assistant");
        replyMessage.setModel(request.getModel());
        replyMessage.setTokens(aiChatResponse.getUsage().getTotal_tokens());
        replyMessage.setContent(aiChatResponse.getChoices().get(0).getMessage().getContent());
        replyMessage.setCreated_time(LocalDateTime.now());
        this.baseMapper.insert(replyMessage);

        // 更新缓存（Redis），把新消息推送到Redis列表中
        updateContextInCache(sessionId, aiChatResponse.getChoices().get(0).getMessage().getContent());
        return aiChatResponse;
    }

    @Override
    public List<AIChatMessage> getMessagesBySessionId(Long sessionId, int limit) {
        QueryWrapper<AIChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", sessionId)
                .eq("is_delete", 0)
                .orderByAsc("created_time");  // 按创建时间降序排序
        return this.baseMapper.selectList(queryWrapper).stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public boolean clearMessages(Long sessionId) {
        QueryWrapper<AIChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", sessionId);
        return this.baseMapper.delete(queryWrapper) > 0;
    }

    // 从缓存中获取上下文（Redis列表）
    private String getContextFromCache(Long sessionId) {
        List<Object> contextList = redisTemplate.opsForList().range(CHAT_CONTEXT_PREFIX + sessionId, 0, -1);
        if (contextList != null && !contextList.isEmpty()) {
            return contextList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" ")); // 拼接成上下文
        }
        return "";
    }

    // 更新缓存中的上下文（推送消息到Redis列表中）
    private void updateContextInCache(Long sessionId, String newMessage) {
        String cacheKey = CHAT_CONTEXT_PREFIX + sessionId;

        // 检查是否存在重复的消息
        List<Object> existingMessages = redisTemplate.opsForList().range(cacheKey, 0, -1);
        if (existingMessages != null && existingMessages.contains(newMessage)) {
            log.info("消息已存在于上下文中，跳过推送: {}", newMessage);
            return;
        }

        // 推送新消息到 Redis
        redisTemplate.opsForList().rightPush(cacheKey, newMessage);
        log.info("新消息已推送到上下文: {}", newMessage);
    }


    public void syncAllContextsToRedis() {
        List<AIChatMessage> allMessages = this.baseMapper.selectList(new QueryWrapper<AIChatMessage>());
        log.info("全量同步到Redis开始，总数{}", allMessages.size());
        for (AIChatMessage message : allMessages) {
            redisTemplate.opsForList().rightPush(CHAT_CONTEXT_PREFIX + message.getSession_id(), message.getContent());
        }
        log.info("全量同步到Redis结束，总数{}", allMessages.size());
    }

    @Override
    public void generateTitleAndSummary(Long sessionId, String content, Long platformId, String model) {
        // 获取会话的第一条消息
        QueryWrapper<AIChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", sessionId)
                .eq("is_delete", 0)
                .orderByAsc("created_time");  // 按时间升序排序，获取第一条消息
        List<AIChatMessage> messages = this.list(queryWrapper);
        AIChatMessage firstMessage = messages.isEmpty() ? null : messages.get(0);


        // 获取第一条消息的内容
        String messageContent = "";
        if (firstMessage == null) {
            messageContent = content;
        } else {
            messageContent = firstMessage.getContent();
        }
        // 构建请求给 AI：标题和概述生成
        String prompt = "现在需要为ai聊天会话起一个标题，请总结【" + messageContent + "】内文字内容（不超过15个字）。注意：保证标题紧扣内容核心，不含多余信息。确保输出准确无误，严格按照以下格式返回。\n" +
                "标题-xxx";

        // 调用 AI 服务生成标题和概述
        String aiResponse = baseAIService.generateByPrompt(prompt, model);

        String title = aiResponse.replace("标题-", "").trim();
        String summary = content.substring(0, Math.min(content.length(), 60)).trim();

        // 更新会话的标题和概述
        QueryWrapper<AIChatSession> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("id", sessionId);
        AIChatSession chatSession = new AIChatSession();
        chatSession.setSession_name(title);
        chatSession.setSummary(summary);
        aiChatSessionMapper.update(chatSession, updateWrapper);
    }
}
