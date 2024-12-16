package com.cpxHelper.myPatients.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAIConfig {

    private List<OpenAIModelConfig> configs; // 여러 설정을 관리하는 리스트

    @Getter
    @Setter
    public static class OpenAIModelConfig {
        private String name; // 설정 이름 (예: default, custom)
        private String model; // GPT 모델 (예: gpt-4, gpt-3.5-turbo)
        private int maxTokens; // 최대 토큰 수
        private double temperature; // 생성 온도
        private Api api; // API 세부 설정

        @Getter
        @Setter
        public static class Api {
            private String key; // API Key
            private String url; // API URL
        }
    }
}
