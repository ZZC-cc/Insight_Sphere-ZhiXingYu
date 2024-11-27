package com.zzc.init.admin.order.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class OrderUpdateRequest implements Serializable {

    /**
     * 订单id
     */
    private long id;

    /**
     * 设置状态
     */
    private String status;

}
