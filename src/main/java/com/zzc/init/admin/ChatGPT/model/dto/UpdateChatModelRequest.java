package com.zzc.init.admin.ChatGPT.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class UpdateChatModelRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty("id")
    Long id;

    @ApiModelProperty("模型")
    String model;

    private static final long serialVersionUID = 1L;

}
