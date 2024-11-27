package com.zzc.init.admin.user.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String username;

    private String password;

    private String geetestChallenge;
    private String geetestValidate;
    private String geetestSeccode;

}
