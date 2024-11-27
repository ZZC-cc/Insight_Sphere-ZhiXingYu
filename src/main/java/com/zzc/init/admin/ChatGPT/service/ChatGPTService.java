package com.zzc.init.admin.ChatGPT.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzc.init.admin.ChatGPT.model.entity.ChatMessage;
import com.zzc.init.admin.ChatGPT.model.entity.ChatSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String apiUrl = "https://api.gptsapi.net/v1/chat/completions";

    @Autowired
    private ChatService chatService;


    public String getChatGPTResponse(List<Map<String, String>> messages, String model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(35000);
        requestFactory.setReadTimeout(35000);

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
            log.warn("Response from OpenAI: " + response.getBody());

            // Ensure the request was successful
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to get response from OpenAI: " + response.getStatusCode());
            }

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("choices")) {
                throw new RuntimeException("Response body is missing 'choices'.");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("No choices returned in the response.");
            }

            String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
            return content;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from OpenAI: " + e.getMessage(), e);
        }
    }


    // 获取会话的主题和消息摘要
    public void generateSessionSummary(Long sessionId, String content) {
        // 获取聊天上下文
        List<ChatMessage> chatMessages = chatService.getChatContext(sessionId);

        if (chatMessages.isEmpty()) {
            throw new RuntimeException("Chat context is empty for session: " + sessionId);
        }

        // 准备发送给 AI 的对话内容
        List<Map<String, String>> messages = chatMessages.stream().map(chatMessage -> {
            Map<String, String> message = new HashMap<>();
            message.put("role", chatMessage.getRole());
            if (chatMessage.getContent().isEmpty()) {
                message.put("content", "你现在是一个AI总结机器人，专门用于对话内容的总结和分拣。{" + content + "}上面这句话是请求。请总结{}里面这段对话的主题，并返回对话的主题和前15个字符,主题不超过15个字。请严格按照回复，不需要回复其他。返回格式为：主题: xxx; 摘要: yyy");
            } else {
                message.put("content", "你现在是一个AI总结机器人，专门用于对话内容的总结和分拣。{" + chatMessage.getContent() + "}上面这句话是请求。请总结{}里面这段对话的主题，并返回对话的主题和前15个字符，主题不超过15个字。请严格按照回复，不需要回复其他。返回格式为：主题: xxx; 摘要: yyy");
            }
            return message;
        }).toList();

        int retryCount = 0;
        final int maxRetries = 3;
        String response = null;
        while (retryCount < maxRetries) {
            try {
                // 调用 ChatGPTService 获取 AI 的响应
                response = getChatGPTResponse(messages, "gpt-4o");

                // 将响应结果分为主题和消息摘要，假设 AI 返回格式为："主题: xxx; 摘要: yyy"
                String[] parts = response.split(";");
                if (parts.length != 2) {
                    throw new RuntimeException("Unexpected response format from AI: " + response);
                }

                String topic = parts[0].replace("主题: ", "").trim();
                String summary = parts[1].replace("摘要: ", "").trim();

                // 更新会话的主题和摘要
                QueryWrapper<ChatSession> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", sessionId);
                ChatSession chatSession = chatService.getBaseMapper().selectOne(queryWrapper);

                if (chatSession != null) {
                    chatSession.setSession_name(topic);
                    chatSession.setSummary(summary);
                    chatService.getBaseMapper().updateById(chatSession);
                }

                // 成功后退出循环
                return;

            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Failed to generate session summary after " + maxRetries + " attempts", e);
                }
            }
        }
    }

    public String generateContent(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "你是一个专业的内容创作助手,这是我需要写的内容，请你使用markdown格式写给我。"),
                Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
            } else {
                throw new RuntimeException("调用 AI 服务失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("AI 服务调用失败", e);
        }
    }

    public String summarizePost(String content) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);


        String prompt = String.format("请总结以下文章的内容并提供简短摘要：\n\n%s", content);


        // 构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o"); // 指定模型
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "你是一个专业的内容总结助手。请你总结一下文章内容。不要分点，一段式总结，不要用markdown语法。"),
                Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
            } else {
                throw new RuntimeException("调用 AI 服务失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("AI 服务调用失败", e);
        }
    }
}
