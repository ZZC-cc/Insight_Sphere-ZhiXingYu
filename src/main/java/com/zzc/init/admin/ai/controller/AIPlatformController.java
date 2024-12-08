package com.zzc.init.admin.ai.controller;

import com.zzc.init.admin.ai.model.entity.AIPlatform;
import com.zzc.init.admin.ai.service.AIPlatformService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ResultUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/platform")
public class AIPlatformController {

    @Autowired
    private AIPlatformService aiPlatformService;

    @ApiOperation("获取所有 AI 平台信息")
    @GetMapping("/list")
    public BaseResponse<List<AIPlatform>> listPlatforms() {
        return ResultUtils.success(aiPlatformService.listPlatforms());
    }

    @ApiOperation("新增 AI 平台信息")
    @PostMapping("/create")
    public BaseResponse<Boolean> createPlatform(@RequestBody AIPlatform platform) {
        return ResultUtils.success(aiPlatformService.createPlatform(platform));
    }

    @ApiOperation("更新 AI 平台信息")
    @PutMapping("/update")
    public BaseResponse<Boolean> updatePlatform(@RequestBody AIPlatform platform) {
        return ResultUtils.success(aiPlatformService.updatePlatform(platform));
    }

    @ApiOperation("删除 AI 平台信息")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deletePlatform(@PathVariable Long id) {
        return ResultUtils.success(aiPlatformService.deletePlatform(id));
    }

    @ApiOperation("根据id获取平台消息")
    @GetMapping("/get/{id}")
    public BaseResponse<AIPlatform> getPlatformById(@PathVariable Long id) {
        return ResultUtils.success(aiPlatformService.thisPlatformById(id));
    }
}
