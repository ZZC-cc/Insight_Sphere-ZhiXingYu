package com.zzc.init.admin.notice.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户添加请求体
 */
@Data
public class DeleteNoticeDto implements Serializable {
    @ApiModelProperty("通知id")
    private Long notice_id;
}
