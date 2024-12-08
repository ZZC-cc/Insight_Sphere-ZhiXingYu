package com.zzc.init.admin.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.ai.model.dto.CreateModelRequest;
import com.zzc.init.admin.ai.model.entity.AIModel;
import com.zzc.init.admin.ai.service.AIModelService;
import com.zzc.init.mapper.AIModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AIModelServiceImpl extends ServiceImpl<AIModelMapper, AIModel> implements AIModelService {


    @Override
    public List<AIModel> listModels() {
        return this.list(new QueryWrapper<AIModel>());
    }

    @Override
    public Boolean createModel(CreateModelRequest request) {
        AIModel aiModel = new AIModel();
        BeanUtils.copyProperties(request, aiModel);
        aiModel.setCreated_time(LocalDateTime.now());
        return this.save(aiModel);
    }

    @Override
    public Boolean updateModel(AIModel model) {
        model.setUpdated_time(LocalDateTime.now());
        return this.updateById(model);
    }

    @Override
    public Boolean deleteModel(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<AIModel> listModelsByPlatformId(Long platformId) {
        QueryWrapper<AIModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("platform_id", platformId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public Long getPlatformIdByModelName(String modelName) {
        QueryWrapper<AIModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("model_name", modelName);
        return this.baseMapper.selectOne(queryWrapper).getPlatform_id();
    }

}
