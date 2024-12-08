package com.zzc.init.admin.order.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class OrderCreateRequest implements Serializable {

    private Long productId;


    /**
     * 支付方式
     */
    private String payMethod;

    private String type;
    private Integer count;


}
