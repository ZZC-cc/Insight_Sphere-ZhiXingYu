package com.zzc.init.admin.orderDetail.model.vo;

import com.zzc.init.admin.order.model.entity.Order;
import com.zzc.init.admin.product.model.vo.ProductVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 商品详细VO
 */
@Data
public class OrderDetailVO implements Serializable {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    private ProductVO product;

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


    /**
     * 包装类转对象
     *
     * @param orderVO
     * @return
     */
    public static OrderDetailVO voToObj(OrderDetailVO orderVO) {
        if (orderVO == null) {
            return null;
        }
        OrderDetailVO order = new OrderDetailVO();
        BeanUtils.copyProperties(orderVO, order);
        return order;
    }

    /**
     * 对象转包装类
     *
     * @param order
     * @return
     */
    public static OrderDetailVO objToVo(Order order) {
        if (order == null) {
            return null;
        }
        OrderDetailVO orderVO = new OrderDetailVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }
}
