package com.zzc.init.admin.alipay.controller;

import com.zzc.init.admin.alipay.model.entity.AlipayTemplate;
import com.zzc.init.admin.order.model.entity.Order;
import com.zzc.init.admin.product.model.entity.Product;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.exception.BusinessException;
import com.zzc.init.mapper.OrderMapper;
import com.zzc.init.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
@Slf4j
public class AlipayController {

    @Autowired
    private AlipayTemplate alipayTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 发起支付
     *
     * @param orderId 订单ID
     * @return 支付跳转页面
     */
    @GetMapping(value = "/pay/{orderId}", produces = "text/html")
    public void pay(@PathVariable Long orderId, HttpServletResponse response) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getStatus() != 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在或已支付");
        }

        try {
            String paymentPage = alipayTemplate.pay(order); // 获取支付页面 HTML
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(paymentPage); // 将 HTML 写入响应
            response.getWriter().flush();
        } catch (Exception e) {
            log.error("支付宝支付接口调用失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付失败，请稍后重试");
        }
    }


    /**
     * 支付宝支付回调通知
     */
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        log.info("接收到支付宝支付异步通知");
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, values) -> log.info("{} -> {}", key, values[0]));

        try {
            // 校验通知参数并处理业务逻辑
            String outTradeNo = request.getParameter("out_trade_no");
            String tradeStatus = request.getParameter("trade_status");

            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                // 更新订单状态为已支付
                Order order = orderMapper.selectById(Long.valueOf(outTradeNo));
                Product product = productMapper.selectById(order.getProductId());
                if (order != null && order.getStatus() == 0) {
                    order.setStatus(1);
                    order.setPaymentMethod("支付宝");
                    order.setPayTime(java.time.LocalDateTime.now());
                    orderMapper.updateById(order);
                    product.setBuyNum(product.getBuyNum() + 1);
                    productMapper.updateById(product);
                }
                return "success";
            }
        } catch (Exception e) {
            log.error("支付宝支付回调处理失败", e);
        }
        return "fail";
    }


    /**
     * 支付完成后的页面跳转
     */
    @GetMapping("/return")
    public void returnPage(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 从支付宝返回的参数中获取订单号
            String outTradeNo = request.getParameter("out_trade_no");
            if (outTradeNo != null) {
                // 构建支付成功页面的跳转 URL，附加订单号
                String redirectUrl = String.format("http://127.0.0.1:9520/success/%s", outTradeNo);
                response.sendRedirect(redirectUrl);
            } else {
                // 如果未获取到订单号，则跳转到一个默认的成功页面
                response.sendRedirect("http://127.0.0.1:9520/success");
            }
        } catch (IOException e) {
            log.error("支付成功页面跳转失败", e);
        }
    }


}