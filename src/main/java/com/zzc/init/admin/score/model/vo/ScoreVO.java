package com.zzc.init.admin.score.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzc.init.admin.task.model.vo.TaskVO;
import com.zzc.init.admin.user.model.vo.UserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 成绩VO
 */
@Data
public class ScoreVO implements Serializable {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 关联评分id
     */
    @ApiModelProperty("关联评分id")
    private Long linkId;

    /**
     * 关联文章
     */
    @ApiModelProperty("关联文章")
    private TaskVO task;

    /**
     * 分数
     */
    @ApiModelProperty("分数")
    private Double score;

    /**
     * 评价
     */
    @ApiModelProperty("评价")
    private String appraisal;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户VO
     */
    @ApiModelProperty("用户VO")
    private UserVO user;

    /**
     * 评分用户 id
     */
    @ApiModelProperty("评分用户 id")
    private Long markUserId;

    /**
     * 评分用户VO
     */
    @ApiModelProperty("评分用户VO")
    private UserVO markUser;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
