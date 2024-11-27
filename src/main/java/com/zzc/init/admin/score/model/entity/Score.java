package com.zzc.init.admin.score.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zzc
 * @date 2024-03-09
 */
@Data
@TableName(value = "score")
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("id")
    @TableField(value = "id")
    private Long id;

    /**
     * 关联评分id
     */
    @ApiModelProperty("关联评分id")
    private Long linkId;

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
     * 评分用户 id
     */
    @ApiModelProperty("评分用户 id")
    private Long markUserId;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

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


