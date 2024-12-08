package com.zzc.init.admin.ai.model.dto;

import lombok.Data;

import java.util.List;

/**
 * AI聊天请求数据传输对象
 */
@Data
public class AIChatRequest {

    /**
     * 使用的模型名称
     */
    private String model;

    /**
     * 会话ID
     */
    private Long request_id;

    /**
     * 消息列表
     */
    private List<Message> messages;

    /**
     * 采样温度，0-2之间
     */
    private Double temperature;

    private Boolean do_sample;


    /**
     * 核采样参数，0-1之间
     */
    private Double top_p;

    /**
     * 生成的回复数量
     */
    private Integer n = 1;

    /**
     * 是否流式返回
     */
    private Boolean stream = false;

    /**
     * 最大令牌数
     */
    private Integer maxTokens;

    /**
     * 存在惩罚系数
     */
    private Double presence_penalty;

    /**
     * 频率惩罚系数
     */
    private Double frequency_penalty;

    /**
     * 聊天消息内部类
     */
    @Data
    public static class Message {
        /**
         * 消息作者的角色，可选值为 "system"、"user" 或 "assistant"
         */
        private String role;

        /**
         * 消息的内容
         */
        private String content;

        /**
         * 消息作者的名称，可选
         */
        private String name;
    }
}
