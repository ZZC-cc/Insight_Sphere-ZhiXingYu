package com.zzc.init.admin.product.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class ProductUpdateRequest implements Serializable {

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

    /**
     * 是否上架
     */
    private Integer isShelves;

    /**
     * 价格
     */
    private Double price;

    /**
     * 库存
     */
    private int stock;
}