package com.zzc.init.admin.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.ai.model.dto.AIChatRequest;
import com.zzc.init.admin.ai.model.dto.AIChatResponse;
import com.zzc.init.admin.ai.model.entity.AIChatMessage;

import java.util.List;

public interface AIChatMessageService extends IService<AIChatMessage> {
    List<AIChatMessage> getMessagesBySessionId(Long sessionId, int limit);

    AIChatResponse sendMessage(AIChatRequest request, Long sessionId);

    boolean clearMessages(Long sessionId);

    public void generateTitleAndSummary(Long sessionId, String content, Long platformId, String model);

}
