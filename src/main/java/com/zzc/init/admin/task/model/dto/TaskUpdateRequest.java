package com.zzc.init.admin.task.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 更新请求
 */
@Data
public class TaskUpdateRequest implements Serializable {

    /**
     * 任务id
     */
    private long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 用户id
     */
    private long userId;

    /**
     * 负责人id
     */
    private long principalId;

    /**
     * 关联id
     */
    private long linkId;

    /**
     * 优先级：高、中、低
     */
    private String priority;

    /**
     * 进度状态：进行中、已完成、延迟
     */
    private String status;

    /**
     * 进度百分比
     */
    private Double progress;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
