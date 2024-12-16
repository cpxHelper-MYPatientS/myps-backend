package com.cpxHelper.myPatients.domain.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PromptLoader {

    // application.yaml에서 여러 프롬프트 경로를 주입
    @Value("#{${prompt.paths:{}}}")
    private Map<String, Resource> promptResources = new HashMap<>();

    // 특정 키를 기반으로 프롬프트 파일 읽기
    public String loadPromptTemplate(String key) {
        Resource resource = promptResources.get(key);
        if (resource == null) {
            throw new RuntimeException("Prompt key not found: " + key);
        }
        return loadPromptTemplateFromResource(resource);
    }

    // 공통 메서드: Resource에서 프롬프트 파일 읽기
    private String loadPromptTemplateFromResource(Resource resource) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString().trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt template", e);
        }
    }
}