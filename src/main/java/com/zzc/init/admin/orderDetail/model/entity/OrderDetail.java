package com.zzc.init.admin.orderDetail.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单详细
 */
@TableName(value = "order_detail")
@Data
public class OrderDetail implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
