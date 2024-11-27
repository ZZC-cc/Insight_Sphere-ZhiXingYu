package com.zzc.init.admin.user.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求体
 */
@Data
public class UpdateByUserRequest implements Serializable {


    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long user_id;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String username;

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
