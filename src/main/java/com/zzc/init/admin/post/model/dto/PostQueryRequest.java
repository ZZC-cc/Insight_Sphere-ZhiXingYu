package com.zzc.init.admin.post.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 */
@Data
public class PostQueryRequest implements Serializable {

    private String searchText;

    private Integer current;

    private Integer pageSize;

    private String sortField;

    private String sortOrder;


}
