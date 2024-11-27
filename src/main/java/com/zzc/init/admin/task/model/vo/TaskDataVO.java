package com.zzc.init.admin.task.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 视图
 */
@Data
public class TaskDataVO implements Serializable {
    //任务条数
    private long taskCount;

    //已完成任务条数
    private long finishTaskCount;

    //进行中任务条数
    private long doingTaskCount;

    //未完成任务条数
    private long unFinishTaskCount;
}
