package com.zzc.init.admin.comment.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class CommentAddRequest implements Serializable {

    /**
     * 内容
     */
    private String content;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 文章id
     */
    private Long post_id;

    /**
     * 父评论id
     */
    private Long parent_id;

    /**
     * 根评论id
     */
    private Long root_parent_id;


    private static final long serialVersionUID = 1L;
}
