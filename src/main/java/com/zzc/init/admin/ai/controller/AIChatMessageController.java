package com.zzc.init.admin.ai.controller;

import com.zzc.init.admin.ai.model.dto.AIChatRequest;
import com.zzc.init.admin.ai.model.dto.AIChatResponse;
import com.zzc.init.admin.ai.model.dto.GetMessageRequest;
import com.zzc.init.admin.ai.model.entity.AIChatMessage;
import com.zzc.init.admin.ai.service.AIChatMessageService;
import com.zzc.init.admin.ai.service.BaseAIService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai/chat")
public class AIChatMessageController {

    @Autowired
    private AIChatMessageService aiChatMessageService;

    @Resource
    private BaseAIService baseAIService;


//    @PostMapping("/message")
//    public BaseResponse<String> sendMessage(@RequestBody SendMessageRequest request) {
//
//        return ResultUtils.success(aiChatMessageService.sendMessage(request.getSessionId(), request.getRole(), request.getContent(), request.getModel(), request.getPlatformId()));
//    }

    @PostMapping("/message/{sessionId}")
    public BaseResponse<AIChatResponse> sendMessage(@RequestBody AIChatRequest request, @PathVariable Long sessionId) {
        return ResultUtils.success(aiChatMessageService.sendMessage(request, sessionId));
    }

    @PostMapping("/message/detail")
    public BaseResponse<List<AIChatMessage>> getMessages(@RequestBody GetMessageRequest request) {
        return ResultUtils.success(aiChatMessageService.getMessagesBySessionId(request.getSessionId(), request.getLimit()));
    }

    @DeleteMapping("/message/{sessionId}")
    public BaseResponse<Boolean> clearMessages(@PathVariable Long sessionId) {
        return ResultUtils.success(aiChatMessageService.clearMessages(sessionId));
    }

    /**
     * 处理搜索请求
     *
     * @param query  用户输入的搜索内容
     * @param stream 是否启用流式返回
     * @return 搜索结果
     */
    @PostMapping("/search")
    public BaseResponse<String> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "false") boolean stream
    ) {
        try {
            log.info("Received search request: query={}, stream={}", query, stream);
            String result = baseAIService.search(query, stream);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("Error while processing search request", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
