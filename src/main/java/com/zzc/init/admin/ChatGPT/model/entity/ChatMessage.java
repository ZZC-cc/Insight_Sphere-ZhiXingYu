// ChatMessage.java
package com.zzc.init.admin.ChatGPT.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("messages")
public class ChatMessage {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("信息id")
    private Long id;

    @ApiModelProperty("聊天id")
    private Long session_id;

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("创建时间")
    private LocalDateTime created_time;

    @TableLogic
    private Integer isDelete;
}