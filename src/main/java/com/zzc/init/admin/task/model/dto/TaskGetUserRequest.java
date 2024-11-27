package com.zzc.init.admin.task.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class TaskGetUserRequest implements Serializable {


    private long userId;

}
