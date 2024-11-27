package com.zzc.init.admin.orderDetail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.orderDetail.model.dto.OrderDetailCreateRequest;
import com.zzc.init.admin.orderDetail.model.entity.OrderDetail;
import com.zzc.init.admin.orderDetail.service.OrderDetailService;
import com.zzc.init.admin.product.model.vo.ProductVO;
import com.zzc.init.admin.product.service.impl.ProductServiceImpl;
import com.zzc.init.mapper.OrderDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单详细服务实现
 */
@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Resource
    private ProductServiceImpl productService;

    /**
     * 创建订单
     */
    @Override
    public boolean createOrderDetail(List<OrderDetailCreateRequest> orderDetail, Long orderId) {
        for (OrderDetailCreateRequest orderDetail1 : orderDetail) {
            OrderDetail orderDetail2 = new OrderDetail();
            BeanUtils.copyProperties(orderDetail1, orderDetail2);
            orderDetail2.setOrderId(orderId);
            this.save(orderDetail2);
        }
        return true;
    }


//    /**
//     * 通过订单id获取订单详情
//     */
//    @Override
//    public List<OrderDetailVO> getOrderDetailByOrderId(Long orderId) {
//        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("orderId", orderId);
//        List<OrderDetail> orderDetails = this.list(queryWrapper);
//        List<OrderDetailVO> orderDetailVOS = orderDetails.stream().map(orderDetail -> {
//            OrderDetailVO orderDetailVO = new OrderDetailVO();
//            BeanUtils.copyProperties(orderDetail, orderDetailVO);
//            return orderDetailVO;
//        }).collect(Collectors.toList());
////        for (OrderDetailVO orderDetailVO : orderDetailVOS) {
////            orderDetailVO.setProduct(productService.getProductById(orderDetailVO.getProductId()));
////        }
//        return orderDetailVOS;
//    }

}




