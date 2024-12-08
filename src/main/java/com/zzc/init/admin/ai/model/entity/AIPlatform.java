package com.zzc.init.admin.ai.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_platform")
public class AIPlatform {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("平台名称")
    private String platform_name;

    @ApiModelProperty("平台描述")
    private String platform_description;

    @ApiModelProperty("平台链接")
    private String platform_url;

    @ApiModelProperty("API基础URL")
    private String api_url;

    @ApiModelProperty("平台封面")
    private String platform_image_url;

    @ApiModelProperty("创建时间")
    private LocalDateTime created_time;

    @ApiModelProperty("更新时间")
    private LocalDateTime updated_time;


    @TableLogic
    @ApiModelProperty("逻辑删除标记")
    @TableField(value = "is_deleted")
    private Integer is_deleted;
}
