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

@Component
@RequiredArgsConstructor
public class PromptLoader {

    // application.yaml에서 설정된 경로 주입
    @Value("${prompt.path:}")
    private Resource promptResource;

    // 프롬프트 파일 읽기
    public String loadPromptTemplate() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(promptResource.getInputStream(), StandardCharsets.UTF_8))) {
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