package com.zzc.init.admin.task.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索请求
 */
@Data
public class TaskCategoryRequest implements Serializable {

    // 分类
    private String category;

    // 是否升序
    private String ascending;

}
