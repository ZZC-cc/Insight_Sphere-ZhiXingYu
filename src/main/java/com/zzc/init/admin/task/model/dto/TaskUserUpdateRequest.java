package com.zzc.init.admin.task.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class TaskUserUpdateRequest implements Serializable {

    /**
     * 任务id
     */
    private long id;

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
    private String progress;
}
