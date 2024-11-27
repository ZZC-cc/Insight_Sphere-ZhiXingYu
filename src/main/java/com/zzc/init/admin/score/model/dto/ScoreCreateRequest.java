package com.zzc.init.admin.score.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class ScoreCreateRequest implements Serializable {

    /**
     * 关联评分id
     */
    @ApiModelProperty("关联评分id")
    private Long linkId;

}
