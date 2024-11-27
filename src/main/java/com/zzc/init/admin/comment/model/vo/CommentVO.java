package com.zzc.init.admin.comment.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzc.init.admin.user.model.vo.UserVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子
 */
@Data
public class CommentVO implements Serializable {

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
    private UserVO user;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 文章id
     */
    private Long post_id;

    private String post_name;

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


    private List<CommentVO> child;    // 本评论下的子评论

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
