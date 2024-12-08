package com.zzc.init.admin.ai.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * AI聊天响应数据传输对象
 */
@Data
@ApiModel(value = "AIChatResponse", description = "AI聊天响应数据传输对象")
public class AIChatResponse {
    /**
     * 聊天完成的唯一标识符
     */
    @ApiModelProperty(value = "聊天完成的唯一标识符", example = "1234567890")
    private String id;

    /**
     * 对象类型，通常为 "chat.completion"
     */
    @ApiModelProperty(value = "对象类型，通常为 'chat.completion'", example = "chat.completion")
    private String object;

    /**
     * 创建时间的时间戳
     */
    @ApiModelProperty(value = "创建时间的时间戳", example = "1234567890")
    private Integer created;

    /**
     * 使用的模型ID
     */
    @ApiModelProperty(value = "使用的模型ID", example = "gpt-3.5-turbo")
    private String model;

    /**
     * 聊天完成的选项列表
     */
    @ApiModelProperty(value = "聊天完成的选项列表")
    private List<Choice> choices;

    /**
     * 令牌使用情况
     */
    @ApiModelProperty(value = "令牌使用情况")
    private Usage usage;

    /**
     * 系统指纹
     */
    @ApiModelProperty(value = "系统指纹", example = "abcdef123456")
    private String system_fingerprint;

    /**
     * 聊天完成选项的内部类
     */
    @Data
    public static class Choice {
        /**
         * 选项的索引
         */
        @ApiModelProperty(value = "选项的索引", example = "0")
        private Integer index;

        /**
         * 消息对象
         */
        @ApiModelProperty(value = "消息对象")
        private Message message;

        /**
         * 日志概率信息
         */
        @ApiModelProperty(value = "日志概率信息")
        private String logprobs;

        /**
         * 完成的原因
         */
        @ApiModelProperty(value = "完成的原因", example = "stop")
        private String finish_reason;
    }

    /**
     * 消息的内部类
     */
    @Data
    public static class Message {
        /**
         * 消息作者的角色
         */
        @ApiModelProperty(value = "消息作者的角色", example = "user")
        private String role;

        /**
         * 消息的内容
         */
        @ApiModelProperty(value = "消息的内容", example = "Hello, world!")
        private String content;
    }

    /**
     * 令牌使用情况的内部类
     */
    @Data
    public static class Usage {
        /**
         * 提示令牌的数量
         */
        @ApiModelProperty(value = "提示令牌的数量", example = "100")
        private Integer prompt_tokens;

        /**
         * 完成令牌的数量
         */
        @ApiModelProperty(value = "完成令牌的数量", example = "50")
        private Integer completion_tokens;

        /**
         * 总令牌数
         */
        @ApiModelProperty(value = "总令牌数", example = "150")
        private Integer total_tokens;

        /**
         * 完成令牌的详细信息
         */
        @ApiModelProperty(value = "完成令牌的详细信息")
        private TokenDetails completion_tokens_details;
    }

    /**
     * 令牌详细信息的内部类
     */
    @Data
    public static class TokenDetails {

        /**
         * 推理令牌数量
         */
        @ApiModelProperty(value = "推理令牌数量", example = "30")
        private Integer reasoning_tokens;

        /**
         * 接受的预测令牌数量
         */
        @ApiModelProperty(value = "接受的预测令牌数量", example = "20")
        private Integer accepted_prediction_tokens;

        /**
         * 拒绝的预测令牌数量
         */
        @ApiModelProperty(value = "拒绝的预测令牌数量", example = "10")
        private Integer rejected_prediction_tokens;
    }
}
