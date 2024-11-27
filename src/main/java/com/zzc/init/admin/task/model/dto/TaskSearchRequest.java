package com.zzc.init.admin.task.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索请求
 */
@Data
public class TaskSearchRequest implements Serializable {


    private String searchText;

}
