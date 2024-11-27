package com.zzc.init.admin.notice.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户添加请求体
 */
@Data
public class UpdateNoticeDto implements Serializable {
    @ApiModelProperty("通知id")
    private Long notice_id;

    @Schema(title = "公告标题")
    private String title;

    @Schema(title = "公告内容")
    private String content;

    @Schema(title = "公告状态 #1：正常，2：禁用")
    private Integer status;

    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_time;

    @Schema(title = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end_time;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "更新用户")
    @TableField("update_user")
    private String update_user;

}
