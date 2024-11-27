package com.zzc.init.admin.order.controller;

import com.zzc.init.admin.order.model.dto.OrderCreateRequest;
import com.zzc.init.admin.order.model.dto.OrderUpdateRequest;
import com.zzc.init.admin.order.model.vo.OrderVO;
import com.zzc.init.admin.order.service.OrderService;
import com.zzc.init.admin.orderDetail.service.OrderDetailService;
import com.zzc.init.admin.user.service.UserService;
import com.zzc.init.common.BaseResponse;
import com.zzc.init.common.DeleteRequest;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderDetailService orderDetailsService;

    @Resource
    private UserService userService;


    /**
     * 创建订单
     */
    @PostMapping("/create")
    public BaseResponse<Long> createOrder(@RequestBody OrderCreateRequest createRequest, HttpServletRequest request) {
        Long orderId = orderService.createOrder(createRequest, request);

        return ResultUtils.success(orderId);
    }

    /**
     * 创建vip订单
     */
    @PostMapping("/create/vip")
    public BaseResponse<Boolean> createVipOrder(@RequestParam int days, HttpServletRequest request) {
        boolean res = orderService.createVipOrder(days, request);
        if (!res) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "创建失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 更新支付状态
     */
    @PostMapping("/update/pay/status")
    public BaseResponse<String> updatePayStatus(@RequestBody OrderUpdateRequest orderUpdateRequest) {
        boolean res = orderService.updatePayStatus(orderUpdateRequest);
        if (!res) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "操作失败");
        }
        return ResultUtils.success("操作成功");
    }

    /**
     * 获取全部订单VO列表
     */
    @PostMapping("/get/all/vo")
    public BaseResponse<List<OrderVO>> getAllOrderVO(HttpServletRequest request) {
        return ResultUtils.success(orderService.getAllOrderVO(request));
    }

    /**
     * 通过id获取订单VO
     */
    @GetMapping("/get/vo")
    public BaseResponse<OrderVO> getOrderVOById(@RequestParam Long id) {
        return ResultUtils.success(OrderVO.objToVo(orderService.getById(id)));
    }

    /**
     * 删除订单
     */
    @PostMapping("/delete")
    public BaseResponse<String> deleteOrder(@RequestBody DeleteRequest deleteRequest) {
        boolean res = orderService.removeById(deleteRequest.getId());
        if (!res) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "删除失败");
        }
        return ResultUtils.success("删除成功");
    }

    /**
     * 多类型搜索
     */
    @GetMapping("/search")
    public BaseResponse<List<OrderVO>> searchOrderBySearchText(@RequestParam(value = "searchText", required = false) String searchText, HttpServletRequest request) {
        return ResultUtils.success(orderService.searchOrderVO(searchText, request));
    }

    /**
     * 获取个人全部订单列表
     */
    @PostMapping("/get/my/all/vo")
    public BaseResponse<List<OrderVO>> getAllMyOrderVO(HttpServletRequest request) {
        return ResultUtils.success(orderService.getMyOrderVO(userService.getLoginUser(request).getUser_id(), request));
    }
}
