package com.zzc.init.admin.task.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzc.init.admin.post.model.vo.PostVO;
import com.zzc.init.admin.user.model.vo.UserVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子视图
 */
@Data
public class TaskVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 用户id
     */
    private long userId;

    private UserVO user;

    /**
     * 负责人id
     */
    private long principalId;

    private UserVO principal;

    /**
     * 关联id
     */
    private long linkId;

    private PostVO post;

    /**
     * 进度状态：进行中、已完成、延迟
     */
    private String status;

    /**
     * 任务完成百分比
     */
    private Double progress;

    /**
     * 优先级：高、中、低
     */
    private String priority;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


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
}
