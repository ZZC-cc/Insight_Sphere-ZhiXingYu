package com.zzc.init.admin.ai.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "ai_chat_message")
public class AIChatMessage {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("消息ID")
    private Long id;

    @ApiModelProperty("会话ID")
    private Long session_id;

    @ApiModelProperty("角色（user/assistant/system）")
    private String role;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("创建时间")
    private LocalDateTime created_time;


    @TableLogic
    @ApiModelProperty("逻辑删除标记")
    @TableField(value = "is_delete")
    private Integer isDelete;

    @ApiModelProperty("消息tokens（用于统计和上下文控制）")
    private Integer tokens;

    @ApiModelProperty("模型名称")
    private String model;

}
