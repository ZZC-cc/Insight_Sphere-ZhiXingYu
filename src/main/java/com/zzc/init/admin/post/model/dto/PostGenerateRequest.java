package com.zzc.init.admin.post.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class PostGenerateRequest implements Serializable {

    /**
     * 提示词
     */
    private String prompt;

    private static final long serialVersionUID = 1L;
}
