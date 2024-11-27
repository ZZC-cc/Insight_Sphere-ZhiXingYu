package com.zzc.init.admin.task.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class TaskGetStatusRequest implements Serializable {


    /**
     * 进度状态：进行中、已完成、延迟
     */
    private String status;

    private long userId;

}
