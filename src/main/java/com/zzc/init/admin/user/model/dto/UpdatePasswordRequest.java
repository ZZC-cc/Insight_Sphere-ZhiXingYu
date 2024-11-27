package com.zzc.init.admin.user.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新密码请求体
 */
@Data
public class UpdatePasswordRequest implements Serializable {


    /**
     * 用户id
     */

    @TableField(value = "user_id")
    private Long user_id;


    /**
     * 旧密码
     */
    @ApiModelProperty("旧密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @ApiModelProperty("新密码")
    private String newPassword;


    /**
     * 确认新密码
     */
    @ApiModelProperty("确认新密码")
    private String confirmPassword;

}
