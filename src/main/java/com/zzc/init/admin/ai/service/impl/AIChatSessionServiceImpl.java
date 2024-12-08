package com.zzc.init.admin.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.ai.model.entity.AIChatSession;
import com.zzc.init.admin.ai.model.vo.AIChatSessionVO;
import com.zzc.init.admin.ai.service.AIChatSessionService;
import com.zzc.init.admin.user.service.impl.UserServiceImpl;
import com.zzc.init.mapper.AIChatMessageMapper;
import com.zzc.init.mapper.AIChatSessionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AIChatSessionServiceImpl extends ServiceImpl<AIChatSessionMapper, AIChatSession> implements AIChatSessionService {

    @Autowired
    private AIChatMessageMapper aiChatMessageMapper;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Long createSession(Long userId, String model) {
        AIChatSession session = new AIChatSession();
        session.setUser_id(userId);
        session.setModel(model);
        session.setCreated_time(LocalDateTime.now());
        this.baseMapper.insert(session);
        return session.getId();
    }

    @Override
    public List<AIChatSessionVO> getUserSessions(Long userId) {
        QueryWrapper<AIChatSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_time");
        List<AIChatSession> sessions = this.baseMapper.selectList(queryWrapper);
        List<AIChatSessionVO> sessionVOS = new ArrayList<>();
        for (AIChatSession session : sessions) {
            AIChatSessionVO sessionVO = new AIChatSessionVO();
            BeanUtils.copyProperties(session, sessionVO);
            sessionVO.setUserVO(userService.getUserVO(session.getUser_id()));
            sessionVOS.add(sessionVO);
        }
        log.warn("getUserSessions:{} " + sessionVOS);
        return sessionVOS;
    }

    @Override
    public int deleteSession(Long sessionId) {
        AIChatSession session = this.baseMapper.selectById(sessionId);
        return this.baseMapper.deleteById(sessionId);
    }

    @Override
    public boolean updateSessionName(Long sessionId, String newName) {
        AIChatSession session = this.baseMapper.selectById(sessionId);
        session.setSession_name(newName);
        return this.baseMapper.updateById(session) > 0;
    }

    @Override
    public Boolean updateSession(AIChatSessionVO sessionVO) {
        QueryWrapper<AIChatSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", sessionVO.getId());
        AIChatSession chatSession = new AIChatSession();
        BeanUtils.copyProperties(sessionVO, chatSession);
        return this.baseMapper.update(chatSession, queryWrapper) > 0;
    }


}
