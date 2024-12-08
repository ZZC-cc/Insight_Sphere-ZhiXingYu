package com.zzc.init.admin.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.ai.model.entity.AIChatSession;
import com.zzc.init.admin.ai.model.vo.AIChatSessionVO;

import java.util.List;

public interface AIChatSessionService extends IService<AIChatSession> {
    Long createSession(Long userId, String model);

    int deleteSession(Long sessionId);

    List<AIChatSessionVO> getUserSessions(Long userId);

    boolean updateSessionName(Long sessionId, String newName);

    Boolean updateSession(AIChatSessionVO sessionVO);
}
