package com.zzc.init.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.init.admin.order.model.dto.OrderCreateRequest;
import com.zzc.init.admin.order.model.dto.OrderUpdateRequest;
import com.zzc.init.admin.order.model.entity.Order;
import com.zzc.init.admin.order.model.vo.OrderVO;
import com.zzc.init.admin.order.service.OrderService;
import com.zzc.init.admin.orderDetail.service.OrderDetailService;
import com.zzc.init.admin.product.model.entity.Product;
import com.zzc.init.admin.product.service.ProductService;
import com.zzc.init.admin.user.model.entity.User;
import com.zzc.init.admin.user.model.vo.UserVO;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.constant.OrderConstant;
import com.zzc.init.exception.BusinessException;
import com.zzc.init.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 订单服务实现
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private UserService userService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private ProductService productService;

    /**
     * 创建订单
     */
    @Override
    public Long createOrder(OrderCreateRequest orderCreateRequest, HttpServletRequest request) {
        // 校验订单请求参数
        if (orderCreateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }
        // 创建订单对象
        Order order = new Order();
        if (orderCreateRequest.getType().equals("普通订单")) {
            Product product = productService.getById(orderCreateRequest.getProductId());
            if (product == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
            }
            order.setProductId(product.getId());
            order.setMoney(product.getPrice());
        } else if (orderCreateRequest.getType().equals("会员订单")) {
            int count = orderCreateRequest.getCount();
            // 计算金额
            Double money;
            if (orderCreateRequest.getType().equals("会员订单")) {
                if (count < 3) {
                    money = 19.9 * count;
                } else if (3 <= count && count < 7) {
                    money = 19.9 * count * 0.8;
                } else {
                    money = 19.9 * count * 0.6;
                }
                order.setMoney(money);
            } else {
                money = 19.9 * count;
            }
        }

        // 获取当前用户信息
        UserVO currentUser = userService.getLoginUser(request);
        order.setUserId(currentUser.getUser_id());
        order.setType(orderCreateRequest.getType());
        order.setCount(orderCreateRequest.getCount());
        order.setStatus(0); // 初始化状态为未支付
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());


        // 保存订单到数据库
        this.save(order);

        return order.getId();
    }

    //开通会员
    @Override
    public boolean createVipOrder(int days, HttpServletRequest request) {
        UserVO userVO = userService.getLoginUser(request);
        User user = userService.getById(userVO.getUser_id());
        if (user.getRole().equals("user")) {
            user.setRole("vip");
            user.setVipStartTime(LocalDateTime.now());
            user.setVipEndTime(LocalDateTime.now().plusDays(days));
            return userService.updateById(user);
        } else {
            user.setVipEndTime(user.getVipEndTime().plusDays(days));
            return userService.updateById(user);
        }
    }

    //vip到期
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public boolean vipExpire() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "vip");
        List<User> users = userService.list(queryWrapper);
        for (User user : users) {
            if (user.getVipStartTime() != null) {
                if (user.getVipEndTime().isBefore(LocalDateTime.now())) {
                    user.setRole("user");
                    userService.updateById(user);
                }
            }
        }
        return true;
    }

    /**
     * 更新支付状态
     */
    @Override
    public boolean updatePayStatus(OrderUpdateRequest updateRequest) {
        Order order = this.getById(updateRequest.getId());
        if (Objects.equals(updateRequest.getStatus(), OrderConstant.PAY)) {
            order.setPayTime(LocalDateTime.now());
            order.setStatus(1);
        } else if (Objects.equals(updateRequest.getStatus(), OrderConstant.CANCEL)) {
            order.setStatus(2);
        } else if (Objects.equals(updateRequest.getStatus(), OrderConstant.CLOSE)) {
            order.setStatus(3);
        } else if (Objects.equals(updateRequest.getStatus(), OrderConstant.FINISH)) {
            order.setStatus(4);
        }

        return updateById(order);
    }

    /**
     * 获取全部订单VO列表
     */
    @Override
    public List<OrderVO> getAllOrderVO(HttpServletRequest request) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        List<Order> orders = this.list(queryWrapper);
        List<OrderVO> orderVOS = orders.stream().map(OrderVO::objToVo).collect(java.util.stream.Collectors.toList());
        for (OrderVO orderVO : orderVOS) {
            orderVO.setUser(userService.getUserByUserId(orderVO.getUserId()));
            orderVO.setProduct(productService.getProductById(orderVO.getProductId(), request));
        }
        return orderVOS;
    }

//    /**
//     * 根据数量订单VO列表
//     */
//    @Override
//    public List<OrderVO> getOrderVOByNumber(int number) {
//        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("createTime").last("limit " + number);
//        List<Order> orders = this.list(queryWrapper);
//        List<OrderVO> orderVOS = orders.stream().map(OrderVO::objToVo).collect(java.util.stream.Collectors.toList());
//        for (OrderVO orderVO : orderVOS) {
//            orderVO.setUser(userService.getUserByUserId(orderVO.getUserId()));
//            orderVO.setProduct(productService.getProductById(orderVO.getProductId()));
//        }
//        return orderVOS;
//    }

    /**
     * 多类型搜索
     */
    @Override
    public List<OrderVO> searchOrderVO(String searchText, HttpServletRequest request) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        queryWrapper.like("name", searchText).or().like("phone", searchText).or().like("address", searchText).like("payMethod", searchText);
        List<Order> orders = this.list(queryWrapper);
        List<OrderVO> orderVOS = orders.stream().map(OrderVO::objToVo).collect(java.util.stream.Collectors.toList());
        for (OrderVO orderVO : orderVOS) {
            orderVO.setUser(userService.getUserByUserId(orderVO.getUserId()));
            orderVO.setProduct(productService.getProductById(orderVO.getProductId(), request));
        }
        return orderVOS;
    }

    /**
     * 根据用户id获取个人全部订单VO
     */
    @Override
    public List<OrderVO> getMyOrderVO(Long user_id, HttpServletRequest request) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createTime");
        queryWrapper.eq("userId", user_id);
        List<Order> orders = this.list(queryWrapper);
        List<OrderVO> orderVOS = orders.stream().map(OrderVO::objToVo).collect(java.util.stream.Collectors.toList());
        for (OrderVO orderVO : orderVOS) {
            orderVO.setUser(userService.getUserByUserId(orderVO.getUserId()));
            orderVO.setProduct(productService.getProductById(orderVO.getProductId(), request));
        }
        return orderVOS;
    }
}




