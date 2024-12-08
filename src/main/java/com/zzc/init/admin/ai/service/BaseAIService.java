package com.zzc.init.admin.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzc.init.admin.ai.model.dto.AIChatRequest;
import com.zzc.init.admin.ai.model.dto.AIChatResponse;
import com.zzc.init.admin.ai.model.entity.AIModel;
import com.zzc.init.admin.ai.model.entity.AIPlatform;
import com.zzc.init.mapper.AIModelMapper;
import com.zzc.init.mapper.AIPlatformMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BaseAIService {

    @Resource
    private RestTemplate restTemplateWithProxy;


    @Resource
    private AIPlatformMapper aiPlatformMapper;

    @Resource
    private AIModelMapper aiModelMapper;

    // 动态获取 API Key 和 URL
    protected AIPlatform getPlatformConfig(Long platformId) {
        return aiPlatformMapper.selectById(platformId);
    }

    public AIChatResponse sendMessage(AIChatRequest request) {

        QueryWrapper<AIModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("model_name", request.getModel());
        AIModel aiModel = aiModelMapper.selectOne(queryWrapper);
        AIPlatform platformConfig = getPlatformConfig(aiModel.getPlatform_id());
        String apiKey = aiModel.getApi_key();
        String chatUrl = "/chat/completions";
        if ("Claude".equals(platformConfig.getPlatform_name())) {
            chatUrl = "/messages";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        log.warn("AIChatRequest:{}", request);


        // 构建请求体
        HttpEntity<AIChatRequest> entity = new HttpEntity<>(request, headers);


        try {
            ResponseEntity<AIChatResponse> response = restTemplateWithProxy.exchange(
                    platformConfig.getApi_url() + chatUrl,
                    HttpMethod.POST,
                    entity,
                    AIChatResponse.class);

            log.warn("response: {}", response);
            if (response.getStatusCode() == HttpStatus.OK) {
                AIChatResponse aiResponse = response.getBody();
                return aiResponse;
            } else {
                throw new RuntimeException("发送消息失败：" + response.getStatusCode() + " " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calling AI API", e);
        }
    }


//    public String sendMessage(Long platformId, String model, String message, String context) {
//        AIPlatform platformConfig = getPlatformConfig(platformId);
//        QueryWrapper<AIModel> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("model_name", model);
//        AIModel aiModel = aiModelMapper.selectOne(queryWrapper);
//        String apiKey = aiModel.getApi_key();
//        String chatUrl = "/chat/completions";
//        if (platformConfig.getPlatform_name() == "Claude") {
//            chatUrl = "/messages";
//        }
//
//        if (platformConfig == null) {
//            throw new IllegalArgumentException("Platform configuration not found for ID: " + platformId);
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(apiKey);
//
//        if (context.isEmpty() && ("Kimi".equals(platformConfig.getPlatform_name()) || "腾讯混元".equals(platformConfig.getPlatform_name()))) {
//            context = "你是一个智能聊天机器人，你将回答我所有的问题，给我专业切有趣的回复。";
//        }
//        // 构建请求体
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", model);
//        requestBody.put("messages", List.of(
//                Map.of("role", "system", "content", context),
//                Map.of("role", "user", "content", message)
//        ));
//        log.warn("requestBody: " + requestBody);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
//
//
//        try {
//            ResponseEntity<Map> response = restTemplateWithProxy.exchange(platformConfig.getApi_url() + chatUrl, HttpMethod.POST, entity, Map.class);
//            log.warn("response: " + response);
//            if (response.getStatusCode() == HttpStatus.OK) {
//                Map<String, Object> responseBody = response.getBody();
//                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
//                return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
//            } else {
//                throw new RuntimeException("发送消息失败：" + response.getStatusCode() + " " + response.getBody());
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error calling AI API", e);
//        }
//    }

    public String generateByPrompt(String prompt, Long platformId, String model) {
        AIPlatform platformConfig = getPlatformConfig(platformId);
        QueryWrapper<AIModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("model_name", model);
        AIModel aiModel = aiModelMapper.selectOne(queryWrapper);
        String apiKey = aiModel.getApi_key();
        String chatUrl = "/chat/completions";
        if (platformConfig.getPlatform_name() == "Claude") {
            chatUrl = "/messages";
        }


        log.warn("platformConfig: " + platformConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);


        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);  // 或者使用其他模型
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "请你根据用户提示词给出准确答复。"),
                Map.of("role", "user", "content", prompt)
        ));

        log.warn("requestBody: " + requestBody);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        log.warn("entity: " + entity);

        try {
            ResponseEntity<Map> response = restTemplateWithProxy.exchange(platformConfig.getApi_url() + chatUrl, HttpMethod.POST, entity, Map.class);
            log.warn("response: " + response);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                log.warn("responseBody: " + responseBody);
                log.warn("choices: " + choices);
                return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
            } else {
                throw new RuntimeException("AI生成内容失败: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("AI生成内容失败", e);
        }
    }

    /**
     * 发送搜索请求
     *
     * @param query  用户查询内容
     * @param stream 是否流式返回
     * @return 搜索结果
     */
    public String search(String query, boolean stream) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("6795add80d7e1327dd31cece37e8c89f.MSK699piZPJ3pA98");

        Map<String, Object> requestBody = buildRequestBody(query, stream);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("Sending Web-Search-Pro request: {}", requestBody);
            ResponseEntity<Map> response = restTemplateWithProxy.exchange(
                    "https://open.bigmodel.cn/api/paas/v4/tools",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return parseSearchResponse(response.getBody());
            } else {
                throw new RuntimeException("Web search failed: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            log.error("Error during Web-Search-Pro API call", e);
            throw new RuntimeException("Web search failed", e);
        }
    }

    /**
     * 构建请求体
     *
     * @param query  查询内容
     * @param stream 是否流式返回
     * @return 请求体 Map
     */
    private Map<String, Object> buildRequestBody(String query, boolean stream) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("request_id", LocalDateTime.now().toString());
        requestBody.put("tool", "web-search-pro");
        requestBody.put("stream", stream);
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", query)
        ));
        return requestBody;
    }

    /**
     * 解析响应
     *
     * @param responseBody 响应体
     * @return 搜索结果
     */
    private String parseSearchResponse(Map<String, Object> responseBody) {
        if (responseBody == null || !responseBody.containsKey("choices")) {
            throw new RuntimeException("Invalid response from Web-Search-Pro API");
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        StringBuilder result = new StringBuilder();

        for (Map<String, Object> choice : choices) {
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            List<Map<String, Object>> toolCalls = (List<Map<String, Object>>) message.get("tool_calls");

            for (Map<String, Object> toolCall : toolCalls) {
                if ("search_result".equals(toolCall.get("type"))) {
                    List<Map<String, Object>> searchResults = (List<Map<String, Object>>) toolCall.get("search_result");
                    for (Map<String, Object> resultItem : searchResults) {
                        result.append("Title: ").append(resultItem.get("title")).append("\n")
                                .append("Link: ").append(resultItem.get("link")).append("\n")
                                .append("Content: ").append(resultItem.get("content")).append("\n\n");
                    }
                }
            }
        }

        return result.toString();
    }
}
