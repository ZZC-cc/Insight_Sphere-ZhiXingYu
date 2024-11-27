package com.zzc.init.admin.product.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教程
 */
@TableName(value = "product")
@Data
public class Product implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 教程图片
     */
    private String images;

    /**
     * 教程描述
     */
    private String description;

    /**
     * 教程内容
     */
    private String content;

    /**
     * 创建者
     */
    private Long userId;

    private int buyNum;

    private int viewsNum;


    /**
     * 标签列表
     */
    private String tags;
    private String type;

    /**
     * 价格
     */
    private Double price;

    /**
     * 库存
     */
    private int stock;

    /**
     * 是否上架
     */
    private Integer isShelves;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
