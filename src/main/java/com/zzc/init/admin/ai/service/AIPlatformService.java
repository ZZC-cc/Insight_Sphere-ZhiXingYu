package com.zzc.init.admin.ai.service;

import com.zzc.init.admin.ai.model.entity.AIPlatform;

import java.util.List;

public interface AIPlatformService {
    List<AIPlatform> listPlatforms();

    Boolean createPlatform(AIPlatform platform);

    Boolean updatePlatform(AIPlatform platform);

    Boolean deletePlatform(Long id);

    AIPlatform thisPlatformById(Long id);

}
