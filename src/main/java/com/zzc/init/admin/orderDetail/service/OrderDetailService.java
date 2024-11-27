package com.zzc.init.admin.orderDetail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.init.admin.orderDetail.model.dto.OrderDetailCreateRequest;
import com.zzc.init.admin.orderDetail.model.entity.OrderDetail;

import java.util.List;

/**
 * 订单详细服务
 */
public interface OrderDetailService extends IService<OrderDetail> {

    boolean createOrderDetail(List<OrderDetailCreateRequest> orderDetail, Long orderId);

//    List<OrderDetailVO> getOrderDetailByOrderId(Long orderId);
}
