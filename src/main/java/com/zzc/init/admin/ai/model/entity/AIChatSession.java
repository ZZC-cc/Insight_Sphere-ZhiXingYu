package com.zzc.init.admin.ai.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "ai_chat_session")
public class AIChatSession {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("会话ID")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long user_id;

    @ApiModelProperty("AI模型")
    private String model;

    @ApiModelProperty("会话名称")
    private String session_name;

    @ApiModelProperty("会话摘要")
    private String summary;

    @ApiModelProperty("会话状态（active/inactive）")
    private String status;

    @ApiModelProperty("创建时间")
    private LocalDateTime created_time;

    @ApiModelProperty("更新时间")
    private LocalDateTime updated_time;

    @TableLogic
    @ApiModelProperty("逻辑删除标记")
    @TableField(value = "is_delete")
    private Integer isDelete;
}
