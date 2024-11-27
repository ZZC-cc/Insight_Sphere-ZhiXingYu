package com.zzc.init.admin.user.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求体
 */
@Data
public class UpdateByAdminRequest implements Serializable {


    /**
     * 用户id
     */

    @TableField(value = "user_id")
    private Long user_id;


    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String username;

    /**
     * 密码(加密)
     */
    @ApiModelProperty("密码(加密)")
    private String password;

    /**
     * 角色
     */
    @ApiModelProperty("角色")
    private String role;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String name;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String mobile;

    /**
     * 账号状态 #1：正常, 2：禁用
     */
    @ApiModelProperty("账号状态 #1：正常, 2：禁用")
    private Integer status;

    /**
     * 家庭住址
     */
    @ApiModelProperty("家庭住址")
    private String address;

    /**
     * 个人简介
     */
    @ApiModelProperty("个人简介")
    private String description;


}
