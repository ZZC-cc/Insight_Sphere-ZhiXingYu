package com.zzc.init.admin.ai.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_model")
public class AIModel {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("AI 平台")
    private Long platform_id;

    @ApiModelProperty("模型名称")
    private String model_name;

    @ApiModelProperty("模型类型")
    private String model_type;

    private String model_desc;

    private String max_output_tokens;

    private String context_window;

    @ApiModelProperty("API密钥")
    private String api_key;


    @ApiModelProperty("费率")
    private Double rate;

    @ApiModelProperty("创建时间")
    private LocalDateTime created_time;

    @ApiModelProperty("更新时间")
    private LocalDateTime updated_time;

    @TableLogic
    @ApiModelProperty("逻辑删除标记")
    @TableField(value = "is_deleted")
    private Integer is_deleted;
}
