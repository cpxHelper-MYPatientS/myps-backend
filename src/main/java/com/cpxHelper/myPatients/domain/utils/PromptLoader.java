package com.cpxHelper.myPatients.domain.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        try (var inputStream = promptResource.getInputStream()) {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt template", e);
        }
    }
}