package com.zzc.init.admin.notice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zzc
 * @date 2024-02-05
 */
@Data
@TableName(value = "notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("通知id")
    @TableField(value = "notice_id")
    @TableId(type = IdType.AUTO)
    private Long notice_id;


    @Schema(title = "公告标题")
    @TableField("title")
    private String title;

    @Schema(title = "公告内容")
    @TableField("content")
    private String content;

    @Schema(title = "创建用户")
    @TableField("create_user")
    private String create_user;

    @Schema(title = "更新用户")
    @TableField("update_user")
    private String update_user;

    @Schema(title = "公告状态 #1：正常，2：禁用")
    @TableField("status")
    private Integer status;

    @Schema(title = "开始时间")
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_time;

    @Schema(title = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("end_time")
    private LocalDateTime end_time;

    @Schema(title = "排序")
    @TableField("sort")
    private Integer sort;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

}


