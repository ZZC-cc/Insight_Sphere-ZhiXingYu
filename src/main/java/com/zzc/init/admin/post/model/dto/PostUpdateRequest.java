package com.zzc.init.admin.post.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 */
@Data
public class PostUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
    private String cover;

    /**
     * 标签列表
     */
    private List<String> tagList;

    private static final long serialVersionUID = 1L;
}
