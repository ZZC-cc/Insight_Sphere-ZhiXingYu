package com.zzc.init.admin.ChatGPT.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户添加请求体
 */
@Data
public class UserMessageRequest implements Serializable {

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    String content;

    @ApiModelProperty("模型")
    String model;

    @ApiModelProperty("会话id")
    Long sessionId;

    private static final long serialVersionUID = 1L;

}
