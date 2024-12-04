package com.cpxHelper.myPatients.domain.utils;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PromptProcessor {

    public String processTemplate(String template, Map<String, String> variables) {
        String processedTemplate = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            processedTemplate = processedTemplate.replace(placeholder, entry.getValue());
        }
        return processedTemplate;
    }
}