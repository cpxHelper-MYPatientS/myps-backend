package com.cpxHelper.myPatients.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class OpenAIConfigManager {

    private final OpenAIConfig openAIConfig;

    public OpenAIConfig.OpenAIModelConfig getConfigByName(String name) {
        return openAIConfig.getConfigs().stream()
                .filter(config -> config.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Config not found for name: " + name));
    }
}

