package com.zzc.init.admin.comment.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子
 */
@TableName(value = "comment")
@Data
public class Comment implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
