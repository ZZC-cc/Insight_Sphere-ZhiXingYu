package com.zzc.init.admin.ChatGPT.controller;


import com.zzc.init.admin.ChatGPT.model.dto.CreateChatRequest;
import com.zzc.init.admin.ChatGPT.model.dto.UpdateChatModelRequest;
import com.zzc.init.admin.ChatGPT.model.dto.UserMessageRequest;
import com.zzc.init.admin.ChatGPT.model.entity.ChatMessage;
import com.zzc.init.admin.ChatGPT.model.entity.ChatSession;
import com.zzc.init.admin.ChatGPT.service.ChatGPTService;
import com.zzc.init.admin.ChatGPT.service.ChatService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatGPTService chatGPTService;

    /**
     * 创建会话
     *
     * @param request
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<ChatSession> createChat(@RequestBody CreateChatRequest request) {
        ChatSession chatSession = chatService.createChatSession(request.getUserId(), request.getModel());
        return ResultUtils.success(chatSession);
    }

    /**
     * 发送消息
     *
     * @param request
     * @return
     */
    @PostMapping("/message")
    public BaseResponse<String> sendMessage(@RequestBody UserMessageRequest request) {
        String content = request.getContent();
        String model = request.getModel();
        Long session_id = request.getSessionId();

        // 保存用户消息
        chatService.saveMessage(session_id, "user", content);

        if (chatService.getBaseMapper().selectById(session_id).getSummary() == null && content != null) {
            chatGPTService.generateSessionSummary(session_id, content);
        }


        // 获取聊天上下文
        List<ChatMessage> chatMessages = chatService.getChatContext(session_id);

        // 将 ChatMessage 转换为 Map<String, String>
        List<Map<String, String>> messages = chatMessages.stream()
                .map(chatMessage -> Map.of(
                        "role", chatMessage.getRole(),
                        "content", chatMessage.getContent()
                ))
                .collect(Collectors.toList());


        // 调用 GPT 服务获取响应
        String response = chatGPTService.getChatGPTResponse(messages, model);

        // 保存 AI 响应消息
        chatService.saveMessage(session_id, "assistant", response);

        return ResultUtils.success(response);
    }

    /**
     * 获取历史会话列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/sessions")
    public BaseResponse<List<ChatSession>> getChatSessions(@RequestParam Long userId) {
        List<ChatSession> sessions = chatService.getChatSessions(userId);
        return ResultUtils.success(sessions);
    }

    /**
     * 获取会话的历史消息列表
     *
     * @param sessionId 会话ID
     * @return 历史消息列表
     */
    @GetMapping("/{sessionId}/messages")
    public BaseResponse<List<ChatMessage>> getChatMessages(@PathVariable Long sessionId) {
        try {
            List<ChatMessage> messages = chatService.getChatMessages(sessionId);
            return ResultUtils.success(messages);
        } catch (Exception e) {
            return ResultUtils.error(500, "获取历史消息失败");
        }
    }

    /**
     * 更新会话模型
     *
     * @param request
     * @return
     */
    @PostMapping("/updateModel")
    public BaseResponse<Boolean> updateChatModel(@RequestBody UpdateChatModelRequest request) {
        Long sessionId = Long.valueOf(request.getId());
        String model = request.getModel();
        if (chatService.updateChatModel(sessionId, model)) {
            return ResultUtils.success(true);
        }
        return ResultUtils.error(500, "更新会话模型失败");
    }

    /**
     * 重命名
     *
     * @param request
     * @return
     */
    @PostMapping("/rename")
    public BaseResponse<Boolean> renameChatSession(@RequestBody Map<String, Object> request) {
        Long sessionId = Long.valueOf(request.get("id").toString());
        String newName = request.get("name").toString();
        if (chatService.renameChatSession(sessionId, newName)) {
            return ResultUtils.success(true);
        }
        return ResultUtils.error(500, "重命名失败");
    }

    /**
     * 删除会话
     *
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChatSession(@RequestBody Map<String, Object> request) {
        Long sessionId = Long.valueOf(request.get("id").toString());
        if (chatService.deleteChatSession(sessionId)) {
            return ResultUtils.success(true);
        }
        return ResultUtils.error(500, "删除失败");
    }
}
