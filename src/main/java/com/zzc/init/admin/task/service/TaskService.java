package com.zzc.init.admin.task.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.task.model.dto.TaskAddRequest;
import com.zzc.init.admin.task.model.dto.TaskUpdateRequest;
import com.zzc.init.admin.task.model.dto.TaskUserUpdateRequest;
import com.zzc.init.admin.task.model.entity.Task;
import com.zzc.init.admin.task.model.vo.TaskDataVO;
import com.zzc.init.admin.task.model.vo.TaskVO;

import java.util.List;

/**
 * 任务服务
 */
public interface TaskService extends IService<Task> {


    boolean addTask(TaskAddRequest taskAddRequest);

    boolean userUpdateStatus(TaskUserUpdateRequest taskUpdateRequest);

    boolean updateTask(TaskUpdateRequest taskUpdateRequest);

    List<TaskVO> getTasksListVO();

    List<TaskVO> getTasksVOBySearchText(String searchText);

    List<TaskVO> getTasksVOByCategory(String category, boolean ascending);

    List<TaskVO> getTasksVOByStatus(String status, long userId);

    List<TaskVO> getTaskByUser(long userId);

    TaskDataVO getTasksData(long userId);

    TaskVO getTaskById(Long id);
}
