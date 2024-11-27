package com.zzc.init.admin.alipay.model.entity;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zzc.init.admin.order.model.entity.Order;
import com.zzc.init.common.ErrorCode;
import com.zzc.init.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlipayTemplate {

    @Value("${alipay.gateway-url}")
    private String gatewayUrl;

    @Value("${alipay.app-id}")
    private String appId;

    @Value("${alipay.merchant-private-key}")
    private String privateKey;

    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    @Value("${alipay.sign-type}")
    private String signType;

    @Value("${alipay.return-url}")
    private String returnUrl;

    @Value("${alipay.notify-url}")
    private String notifyUrl;

    public String pay(Order order) {
        log.info("开始处理支付宝支付，订单ID: {}", order.getId());

        // 初始化支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                gatewayUrl,
                appId,
                privateKey,
                "json",
                "UTF-8",
                alipayPublicKey,
                signType
        );

        // 构造请求
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);

        // 构造订单支付参数
        String bizContent = String.format(
                "{" +
                        "\"out_trade_no\":\"%s\"," +
                        "\"total_amount\":\"%s\"," +
                        "\"subject\":\"%s\"," +
                        "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                        "}",
                order.getId(),
                order.getMoney(),
                "商品支付：" + order.getId()
        );

        alipayRequest.setBizContent(bizContent);

        log.info("支付宝支付请求参数: {}", bizContent);

        try {
            // 发起支付请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            if (result == null || result.trim().isEmpty()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝返回结果为空，请检查配置");
            }

            log.info("支付宝支付页面生成成功");
            return result;

        } catch (AlipayApiException e) {
            log.error("调用支付宝支付接口失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用支付宝支付接口失败");
        }
    }
}
