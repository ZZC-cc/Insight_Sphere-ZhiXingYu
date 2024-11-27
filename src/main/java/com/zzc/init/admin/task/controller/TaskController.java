package com.zzc.init.admin.task.controller;

import com.zzc.init.admin.score.service.ScoreService;
import com.zzc.init.admin.task.model.dto.*;
import com.zzc.init.admin.task.model.vo.TaskDataVO;
import com.zzc.init.admin.task.model.vo.TaskVO;
import com.zzc.init.admin.task.service.TaskService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.DeleteRequest;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import com.zzc.init.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务接口
 */
@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {

    @Resource
    private TaskService taskService;

    @Resource
    private ScoreService scoreService;

    /**
     * 创建任务接口
     */
    @PostMapping("/add")
    @Operation(summary = "创建任务")
    public BaseResponse<String> addTask(@RequestBody TaskAddRequest taskAddRequest) {
        if (!taskService.addTask(taskAddRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建任务失败");
        }
        return ResultUtils.success("创建任务成功");
    }

    /**
     * 更新任务接口
     */
    @PostMapping("/update")
    @Operation(summary = "更新任务")
    public BaseResponse<String> updateTask(@RequestBody TaskUpdateRequest taskUpdateRequest) {
        if (!taskService.updateTask(taskUpdateRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新任务失败");
        }
        if (taskUpdateRequest.getProgress() >= 100.00) {
            scoreService.createScore(taskUpdateRequest.getId());
        }
        return ResultUtils.success("更新任务成功");
    }

    /**
     * 获取任务VO列表
     */
    @PostMapping("/list/vo")
    @Operation(summary = "获取任务VO列表")
    public BaseResponse<List<TaskVO>> listTask() {
        return ResultUtils.success(taskService.getTasksListVO());
    }

    /**
     * 多类型搜索
     */
    @PostMapping("/search")
    @Operation(summary = "多类型搜索")
    public BaseResponse<List<TaskVO>> searchTask(@RequestBody TaskSearchRequest searchRequest) {
        if (searchRequest.getSearchText() == "") {
            return ResultUtils.success(taskService.getTasksListVO());
        }
        return ResultUtils.success(taskService.getTasksVOBySearchText(searchRequest.getSearchText()));
    }

    /**
     * 条件筛选
     */
    @PostMapping("/condition")
    @Operation(summary = "条件筛选")
    public BaseResponse<List<TaskVO>> searchTaskByCondition(@RequestBody TaskCategoryRequest categoryRequest) {
        if (categoryRequest.getCategory() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "排序类型");
        }
        return ResultUtils.success(taskService.getTasksVOByCategory(categoryRequest.getCategory(), Boolean.parseBoolean(categoryRequest.getAscending())));
    }

    /**
     * 删除任务
     */
    @PostMapping("/delete")
    @Operation(summary = "删除任务")
    public BaseResponse<String> deleteTask(@RequestBody DeleteRequest deleteRequest) {
        boolean b = taskService.removeById(deleteRequest.getId());
        if (!b) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除任务失败");
        }
        return ResultUtils.success("删除任务成功");
    }

    /**
     * 根据状态获取任务
     */
    @PostMapping("/get/task/status")
    @Operation(summary = "根据状态获取任务")
    public BaseResponse<List<TaskVO>> getTaskByStatus(@RequestBody TaskGetStatusRequest request) {
        return ResultUtils.success(taskService.getTasksVOByStatus(request.getStatus(), request.getUserId()));
    }

    /**
     * 获取个人的全部任务
     */
    @PostMapping("/get/task/user")
    @Operation(summary = "获取个人的全部任务")
    public BaseResponse<List<TaskVO>> getTaskByUser(@RequestBody TaskGetUserRequest request) {
        return ResultUtils.success(taskService.getTaskByUser(request.getUserId()));
    }

    /**
     * 获取任务统计数据
     */
    @PostMapping("/get/task/data")
    @Operation(summary = "获取任务统计数据")
    public BaseResponse<TaskDataVO> getTaskData(@RequestBody TaskGetUserRequest request) {
        return ResultUtils.success(taskService.getTasksData(request.getUserId()));
    }


}
