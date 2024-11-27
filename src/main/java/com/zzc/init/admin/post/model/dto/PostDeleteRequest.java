package com.zzc.init.admin.post.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class PostDeleteRequest implements Serializable {

    /**
     * id
     */
    private Long post_id;

}
