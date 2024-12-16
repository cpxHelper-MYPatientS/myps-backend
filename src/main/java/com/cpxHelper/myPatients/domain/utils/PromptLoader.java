package com.cpxHelper.myPatients.domain.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class PromptLoader {

    private final ResourceLoader resourceLoader;

    // 기본 프롬프트 경로 설정 (application.yaml에서 설정 가능)
    @Value("${prompt.base-path:prompts/}")
    private String basePath;

    // 특정 이름의 프롬프트 파일 읽기
    public String loadPromptTemplate(String promptName) {
        String resourcePath = basePath + promptName + ".txt"; // 예: prompts/example.txt
        Resource promptResource = resourceLoader.getResource("classpath:" + resourcePath);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(promptResource.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString().trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt template: " + promptName, e);
        }
    }
}