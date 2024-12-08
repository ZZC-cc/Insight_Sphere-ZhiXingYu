package com.zzc.init.admin.ai.controller;

import com.zzc.init.admin.ai.model.dto.CreateModelRequest;
import com.zzc.init.admin.ai.model.entity.AIModel;
import com.zzc.init.admin.ai.service.AIModelService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.ResultUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/model")
public class AIModelController {

    @Autowired
    private AIModelService aiModelService;

    @ApiOperation("获取所有 AI 模型信息")
    @GetMapping("/list")
    public BaseResponse<List<AIModel>> listModels() {
        return ResultUtils.success(aiModelService.listModels());
    }

    @ApiOperation("根据平台id获取对应 AI 模型信息")
    @GetMapping("/listByPlatformId")
    public BaseResponse<List<AIModel>> listModelsByPlatformId(@RequestParam Long platformId) {
        return ResultUtils.success(aiModelService.listModelsByPlatformId(platformId));
    }

    @ApiOperation("新增 AI 模型信息")
    @PostMapping("/create")
    public BaseResponse<Boolean> createModel(@RequestBody CreateModelRequest request) {
        return ResultUtils.success(aiModelService.createModel(request));
    }

    @ApiOperation("更新 AI 模型信息")
    @PutMapping("/update")
    public BaseResponse<Boolean> updateModel(@RequestBody AIModel model) {
        return ResultUtils.success(aiModelService.updateModel(model));
    }

    @ApiOperation("删除 AI 模型信息")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteModel(@PathVariable Long id) {
        return ResultUtils.success(aiModelService.deleteModel(id));
    }

    @ApiOperation("根据模型名称返回平台id")
    @GetMapping("/getPlatformIdByModelName")
    public BaseResponse<Long> getPlatformIdByModelName(@RequestParam String modelName) {
        return ResultUtils.success(aiModelService.getPlatformIdByModelName(modelName));
    }
}
