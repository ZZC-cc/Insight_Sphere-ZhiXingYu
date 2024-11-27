// ChatSession.java
package com.zzc.init.admin.ChatGPT.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sessions")
public class ChatSession {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("聊天id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long user_id;

    @ApiModelProperty("模型")
    private String model; // gpt-3.5-turbo, gpt-4 等

    @ApiModelProperty("会话名称")
    private String session_name;

    @ApiModelProperty("会话图像")
    private String session_image;

    @ApiModelProperty("创建时间")
    private LocalDateTime created_time;

    @ApiModelProperty("概要")
    private String summary;

    @TableLogic
    private Integer isDelete;
}