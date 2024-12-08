package com.zzc.init.admin.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.ai.model.entity.AIChatSession;
import com.zzc.init.admin.ai.model.entity.AIModel;
import com.zzc.init.admin.ai.model.entity.AIPlatform;
import com.zzc.init.admin.ai.service.AIPlatformService;
import com.zzc.init.mapper.AIChatSessionMapper;
import com.zzc.init.mapper.AIModelMapper;
import com.zzc.init.mapper.AIPlatformMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AIPlatformServiceImpl extends ServiceImpl<AIPlatformMapper, AIPlatform> implements AIPlatformService {

    @Resource
    private AIChatSessionMapper aiChatSessionMapper;

    @Resource
    private AIModelMapper aiModelMapper;


    @Override
    public List<AIPlatform> listPlatforms() {
        QueryWrapper<AIPlatform> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("platform_name");
        return this.list(queryWrapper);
    }

    @Override
    public Boolean createPlatform(AIPlatform platform) {
        platform.setCreated_time(LocalDateTime.now());
        platform.setUpdated_time(LocalDateTime.now());
        return this.save(platform);
    }

    @Override
    public Boolean updatePlatform(AIPlatform platform) {
        platform.setUpdated_time(LocalDateTime.now());
        return this.updateById(platform);
    }

    @Override
    public Boolean deletePlatform(Long id) {
        return this.removeById(id);
    }

    @Override
    public AIPlatform thisPlatformById(Long id) {
        return this.getById(id);
    }


}
