package com.zzc.init.admin.ChatGPT.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.ChatGPT.model.entity.ChatMessage;
import com.zzc.init.admin.ChatGPT.model.entity.ChatSession;
import com.zzc.init.mapper.ChatMessageMapper;
import com.zzc.init.mapper.ChatSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ChatService extends ServiceImpl<ChatSessionMapper, ChatSession> {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    // 创建新的聊天
    public ChatSession createChatSession(Long userId, String model) {
        ChatSession chatSession = new ChatSession();
        chatSession.setUser_id(userId);
        chatSession.setModel(model);
        chatSession.setSession_image("https://zcc-1305301692.cos.ap-guangzhou.myqcloud.com/cclogo.png");
        chatSession.setSession_name("新会话" + new Date().getTime());
        chatSession.setCreated_time(LocalDateTime.now());
        this.baseMapper.insert(chatSession);
        return chatSession;
    }

    // 保存消息
    public void saveMessage(Long sessionId, String role, String content) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSession_id(sessionId);
        chatMessage.setRole(role);
        chatMessage.setContent(content);
        chatMessage.setCreated_time(LocalDateTime.now());
        chatMessageMapper.insert(chatMessage);
    }

    // 获取聊天上下文
    public List<ChatMessage> getChatContext(Long sessionId) {
        return chatMessageMapper.selectList(
                new QueryWrapper<ChatMessage>().eq("session_id", sessionId).orderByAsc("created_time")
        );
    }

    public List<ChatSession> getChatSessions(Long userId) {
        QueryWrapper<ChatSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_time");
        return this.baseMapper.selectList(queryWrapper);
    }


    public List<ChatMessage> getChatMessages(Long sessionId) {
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", sessionId);
        return chatMessageMapper.selectList(queryWrapper);
    }

    // 更新聊天会话的模型
    public Boolean updateChatModel(Long sessionId, String model) {
        QueryWrapper<ChatSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", sessionId);
        ChatSession chatSession = this.baseMapper.selectOne(queryWrapper);
        chatSession.setModel(model);

        if (this.baseMapper.updateById(chatSession) == 1) {
            return true;
        }
        return false;
    }

    // 重命名会话
    public Boolean renameChatSession(Long sessionId, String newName) {
        ChatSession session = this.baseMapper.selectById(sessionId);
        if (session != null) {
            session.setSession_name(newName);
            return this.baseMapper.updateById(session) == 1;
        }
        return false;
    }

    // 删除会话
    public Boolean deleteChatSession(Long sessionId) {
        // 删除会话记录
        int sessionDeleted = this.baseMapper.deleteById(sessionId);
        // 删除相关消息记录
        for (ChatMessage session_id : chatMessageMapper.selectList(new QueryWrapper<ChatMessage>().eq("session_id", sessionId))) {
            chatMessageMapper.deleteById(session_id);
        }
        return sessionDeleted > 0;
    }


}
