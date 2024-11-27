package com.zzc.init.admin.score.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class ScoreUpdateRequest implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    /**
     * 分数
     */
    @ApiModelProperty("分数")
    private Double score;

    /**
     * 评价
     */
    @ApiModelProperty("评价")
    private String appraisal;

}
