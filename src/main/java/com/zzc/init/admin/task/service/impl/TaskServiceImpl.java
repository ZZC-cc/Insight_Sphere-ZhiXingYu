package com.zzc.init.admin.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.post.service.PostService;
import com.zzc.init.admin.task.model.dto.TaskAddRequest;
import com.zzc.init.admin.task.model.dto.TaskUpdateRequest;
import com.zzc.init.admin.task.model.dto.TaskUserUpdateRequest;
import com.zzc.init.admin.task.model.entity.Task;
import com.zzc.init.admin.task.model.vo.TaskDataVO;
import com.zzc.init.admin.task.model.vo.TaskVO;
import com.zzc.init.admin.task.service.TaskService;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务服务实现
 */
@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    /**
     * 创建任务
     */
    @Override
    public boolean addTask(TaskAddRequest taskAddRequest) {
        Task task = new Task();
        BeanUtils.copyProperties(taskAddRequest, task);
        task.setProgress(0.00);
        return save(task);
    }

    /**
     * 用户修改任务基本状态
     */
    @Override
    public boolean userUpdateStatus(TaskUserUpdateRequest taskUpdateRequest) {
        Task task = this.getById(taskUpdateRequest.getId());
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        if (!taskUpdateRequest.getStatus().isEmpty()) {
            task.setStatus(taskUpdateRequest.getStatus());
        } else if (!taskUpdateRequest.getPriority().isEmpty()) {
            task.setPriority(taskUpdateRequest.getPriority());
        } else if (!taskUpdateRequest.getProgress().isEmpty()) {
            task.setProgress(Double.parseDouble(taskUpdateRequest.getProgress()));
        }
        return updateById(task);
    }

    /**
     * 修改任务
     */
    @Override
    public boolean updateTask(TaskUpdateRequest taskUpdateRequest) {
        Task task = this.getById(taskUpdateRequest.getId());
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        BeanUtils.copyProperties(taskUpdateRequest, task);
        if (taskUpdateRequest.getProgress() >= 100) {
            task.setStatus("已完成");
            task.setProgress(100.00);
        }
        return updateById(task);
    }

    /**
     * 获取任务VO列表
     */
    @Override
    public List<TaskVO> getTasksListVO() {
        List<Task> tasks = this.list();
        List<TaskVO> taskVOList = tasks.stream().map(task -> {
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            taskVO.setUser(userService.getUserByUserId(task.getUserId()));
            taskVO.setPrincipal(userService.getUserByUserId(task.getPrincipalId()));
//            taskVO.setPost(postService.getPostById(task.getLinkId()));
            return taskVO;
        }).collect(Collectors.toList());
        return taskVOList;
    }

    /**
     * 多类型搜索
     */
    @Override
    public List<TaskVO> getTasksVOBySearchText(String searchText) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("taskName", searchText);
        List<Task> tasks = this.list(queryWrapper);
        List<TaskVO> taskVOList = tasks.stream().map(task -> {
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            taskVO.setUser(userService.getUserByUserId(task.getUserId()));
            taskVO.setPrincipal(userService.getUserByUserId(task.getPrincipalId()));
//            taskVO.setPost(postService.getPostById(task.getLinkId()));
            return taskVO;
        }).collect(Collectors.toList());
        return taskVOList;
    }

    /**
     * 条件筛选
     */
    @Override
    public List<TaskVO> getTasksVOByCategory(String category, boolean ascending) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(category, category);
        if (ascending) {
            queryWrapper.orderByAsc("id");
        } else {
            queryWrapper.orderByDesc("id");
        }
        List<Task> tasks = this.list(queryWrapper);
        List<TaskVO> taskVOList = tasks.stream().map(task -> {
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            taskVO.setUser(userService.getUserByUserId(task.getUserId()));
            taskVO.setPrincipal(userService.getUserByUserId(task.getPrincipalId()));
//            taskVO.setPost(postService.getPostById(task.getLinkId()));
            return taskVO;
        }).collect(Collectors.toList());
        return taskVOList;
    }

    /**
     * 根据状态获取任务
     */
    @Override
    public List<TaskVO> getTasksVOByStatus(String status, long userId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        if (userId != 0) {
            queryWrapper.eq("userId", userId);
        }
        List<Task> tasks = this.list(queryWrapper);
        List<TaskVO> taskVOList = tasks.stream().map(task -> {
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            taskVO.setUser(userService.getUserByUserId(task.getUserId()));
            taskVO.setPrincipal(userService.getUserByUserId(task.getPrincipalId()));
//            taskVO.setPost(postService.getPostById(task.getLinkId()));
            return taskVO;
        }).collect(Collectors.toList());
        return taskVOList;
    }

    /**
     * 获取个人的全部任务
     */
    @Override
    public List<TaskVO> getTaskByUser(long userId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Task> tasks = this.list(queryWrapper);
        List<TaskVO> taskVOList = tasks.stream().map(task -> {
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            taskVO.setUser(userService.getUserByUserId(task.getUserId()));
            taskVO.setPrincipal(userService.getUserByUserId(task.getPrincipalId()));
//            taskVO.setPost(postService.getPostById(task.getLinkId()));
            return taskVO;
        }).collect(Collectors.toList());
        return taskVOList;
    }

    /**
     * 获取任务数据
     */
    @Override
    public TaskDataVO getTasksData(long userId) {
        TaskDataVO taskDataVO = new TaskDataVO();
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        //获取用户任务总条数
        List<Task> tasks = this.list(queryWrapper);
        taskDataVO.setTaskCount(tasks.size());
        queryWrapper.eq("status", "已完成");
        for (Task task : tasks) {
            if (task.getStatus().equals("已完成")) {
                taskDataVO.setFinishTaskCount(taskDataVO.getFinishTaskCount() + 1);
            } else if (task.getStatus().equals("进行中")) {
                taskDataVO.setDoingTaskCount(taskDataVO.getDoingTaskCount() + 1);
            } else if (task.getStatus().equals("未开始")) {
                taskDataVO.setUnFinishTaskCount(taskDataVO.getUnFinishTaskCount() + 1);
            }
        }
        return taskDataVO;
    }

    /**
     * 根据id获取任务VO
     */
    @Override
    public TaskVO getTaskById(Long id) {
        Task task = this.getById(id);
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task, taskVO);
        taskVO.setUser(userService.getUserByUserId(task.getUserId()));
        taskVO.setPrincipal(userService.getUserByUserId(task.getPrincipalId()));
//        taskVO.setPost(postService.getPostById(task.getLinkId()));
        return taskVO;
    }
}




