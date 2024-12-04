package com.cpxHelper.myPatients.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAIConfig {

    private String model; // GPT 모델 (예: gpt-4o, gpt-3.5-turbo)
    private int maxTokens; // 최대 토큰 수
    private double temperature; // 생성 온도

    private Api api;

    @Getter
    @Setter
    public static class Api {
        private String key; // API Key
        private String url; // API URL
    }
}

