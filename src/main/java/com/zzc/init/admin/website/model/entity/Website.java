package com.zzc.init.admin.website.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "website_config")
public class Website implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("id")
    @TableField(value = "id")
    private Long id;


    /**
     * 网站名称
     */
    @ApiModelProperty("网站名称")
    private String site_name;

    /**
     * 网站描述
     */
    @ApiModelProperty("网站描述")
    private String site_description;

    /**
     * logo
     */
    @ApiModelProperty("logo")
    private String logo_url;

    /**
     * 网站主图
     */
    @ApiModelProperty("cover_url")
    private String cover_url;

    /**
     * footer
     */
    @ApiModelProperty("页脚信息")
    private String footer_info;

    /**
     * 访问量
     */
    @ApiModelProperty("访问量")
    private String visit_count;

    /**
     * 公司联系地址
     */
    @ApiModelProperty("公司地址")
    private String contact_address;

    /**
     * 公司邮箱
     */
    @ApiModelProperty("公司邮箱")
    private String contact_email;

    /**
     * 公司电话
     */
    @ApiModelProperty("公司电话")
    private String contact_phone;

    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String contact_name;

    @ApiModelProperty("网站特点描述")
    private String features;


}


