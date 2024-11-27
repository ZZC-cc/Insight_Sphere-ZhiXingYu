package com.zzc.init.admin.order.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzc.init.admin.order.model.entity.Order;
import com.zzc.init.admin.product.model.vo.ProductVO;
import com.zzc.init.admin.user.model.vo.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品VO
 */
@Data
public class OrderVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private UserVO user;
    private ProductVO product;
    private Long productId;


    /**
     * 支付金额
     */
    private Double money;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 0 - 未支付 1 - 已支付
     */
    private Integer status;


    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

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


    /**
     * 包装类转对象
     *
     * @param orderVO
     * @return
     */
    public static OrderVO voToObj(OrderVO orderVO) {
        if (orderVO == null) {
            return null;
        }
        OrderVO order = new OrderVO();
        BeanUtils.copyProperties(orderVO, order);
        return order;
    }

    /**
     * 对象转包装类
     *
     * @param order
     * @return
     */
    public static OrderVO objToVo(Order order) {
        if (order == null) {
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }
}
