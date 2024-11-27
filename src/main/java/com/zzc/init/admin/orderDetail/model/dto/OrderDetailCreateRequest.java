package com.zzc.init.admin.orderDetail.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class OrderDetailCreateRequest implements Serializable {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品数量
     */
    private int quantity;


    /**
     * 商品单价
     */
    private Double price;

    /**
     * 小计金额
     */
    private Double total;
}
