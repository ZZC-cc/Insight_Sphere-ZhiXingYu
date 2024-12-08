package com.zzc.init.admin.ai.service;

import com.zzc.init.admin.ai.model.dto.CreateModelRequest;
import com.zzc.init.admin.ai.model.entity.AIModel;

import java.util.List;

public interface AIModelService {
    List<AIModel> listModels();

    Boolean createModel(CreateModelRequest model);

    Boolean updateModel(AIModel model);

    Boolean deleteModel(Long id);

    List<AIModel> listModelsByPlatformId(Long platformId);

    Long getPlatformIdByModelName(String modelName);
}
