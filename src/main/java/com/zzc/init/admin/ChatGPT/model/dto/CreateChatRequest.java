package com.zzc.init.admin.ChatGPT.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户添加请求体
 */
@Data
public class CreateChatRequest implements Serializable {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    Long userId;

    @ApiModelProperty("模型")
    String model;

    private static final long serialVersionUID = 1L;

}
