package com.zzc.init.admin.user.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzc.init.admin.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户视图（脱敏）
 *
 * @author zzc
 * @date 2024-02-05
 */
@Data
@TableName(value = "user")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("用户id")
    private Long user_id;


    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String username;

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
     * 性别 0：保密 ,1：男 ,2：女
     */
    @ApiModelProperty("性别")
    private String sex;

    private String token;

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

    /**
     * token
     */
    @ApiModelProperty("是否vip")
    private int isVip;

    @ApiModelProperty("vip开始时间")
    private LocalDateTime vipStartTime;

    @ApiModelProperty("vip到期时间")
    private LocalDateTime vipEndTime;


    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 从 User 对象转换为 UserVO 对象
     *
     * @param user 原始 User 对象
     * @return UserVO 对象
     */
    public static UserVO convertToUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        userVO.setUser_id(user.getUser_id());
        userVO.setUsername(user.getUsername());
        userVO.setRole(user.getRole());
        userVO.setAvatar(user.getAvatar());
        userVO.setName(user.getName());
        userVO.setSex(user.getSex());
        userVO.setEmail(user.getEmail());
        userVO.setMobile(user.getMobile());
        userVO.setStatus(user.getStatus());
        userVO.setAddress(user.getAddress());
        userVO.setDescription(user.getDescription());
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        // 若有其他字段可添加
        return userVO;
    }

}


