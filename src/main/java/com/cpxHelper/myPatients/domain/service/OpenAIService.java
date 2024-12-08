package com.cpxHelper.myPatients.domain.service;

import com.cpxHelper.myPatients.config.OpenAIConfig;
import com.cpxHelper.myPatients.domain.utils.PromptProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final OpenAIConfig openAiConfig;

    public String callGptApi(List<Map<String, String>> messages) {
        // HTTP 요청 본문 생성
        Map<String, Object> requestBody = Map.of(
                "model", openAiConfig.getModel(),
                "messages", messages,
                "max_tokens", openAiConfig.getMaxTokens(),
                "temperature", openAiConfig.getTemperature()
        );

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiConfig.getApi().getKey());
        headers.set("Content-Type", "application/json");

        // OpenAI API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                openAiConfig.getApi().getUrl(),
                HttpMethod.POST,
                entity,
                Map.class
        );

        // OpenAI 응답 처리
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("choices")) {
            Map<String, Object> choice = ((List<Map<String, Object>>) responseBody.get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            return (String) message.get("content");
        }

        throw new RuntimeException("Failed to retrieve response from OpenAI API");
    }
}
