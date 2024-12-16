package com.cpxHelper.myPatients.domain.service;

import com.cpxHelper.myPatients.domain.entity.chat.Chat;
import com.cpxHelper.myPatients.domain.entity.checklist.ChecklistItem;
import com.cpxHelper.myPatients.domain.utils.PromptLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final OpenAIService openAiService;
    private final PromptLoader promptLoader;

    public Map<Long, Integer> evaluateChecklist(List<Chat> chatHistory, List<ChecklistItem> checklistItems) {
        // 1. 프롬프트 템플릿 로드
        String promptTemplate = promptLoader.loadPromptTemplate("checklist_prompt");

        // 2. 메시지 생성
        String prompt = buildPrompt(chatHistory, checklistItems, promptTemplate);

        // 3. GPT 호출
        String gptResponse = openAiService.callGptApi(List.of(
                Map.of("role", "user", "content", prompt)
        ));

        // 4. GPT 응답 파싱 및 수행 여부 매핑
        return parseGptResponse(gptResponse, checklistItems);
    }

    private String buildPrompt(List<Chat> chatHistory, List<ChecklistItem> checklistItems, String promptTemplate) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append(promptTemplate).append("\n\n");

        // 체크리스트 항목 추가
        Map<String, List<String>> categorizedItems = categorizeChecklistItems(checklistItems);
        for (Map.Entry<String, List<String>> entry : categorizedItems.entrySet()) {
            promptBuilder.append("\"" + entry.getKey() + "\": [\n");
            for (String item : entry.getValue()) {
                promptBuilder.append("  {{ \"항목\": \"" + item + "\", \"수행\": null }},\n");
            }
            promptBuilder.append("],\n");
        }

        // 대화 내역 추가
        promptBuilder.append("\n대화 기록:\n");
        for (Chat chat : chatHistory) {
            promptBuilder.append(chat.getSenderRole().equals("user") ? "사용자" : "환자").append(": ").append(chat.getMessage()).append("\n");
        }

        return promptBuilder.toString();
    }

    private Map<String, List<String>> categorizeChecklistItems(List<ChecklistItem> checklistItems) {
        Map<String, List<String>> categorizedItems = new HashMap<>();
        for (ChecklistItem item : checklistItems) {
            categorizedItems.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item.getContent());
        }
        return categorizedItems;
    }

    private Map<Long, Integer> parseGptResponse(String gptResponse, List<ChecklistItem> checklistItems) {
        Map<Long, Integer> evaluationResult = new HashMap<>();

        try {
            // GPT 응답(JSON)을 파싱하여 체크리스트 항목과 수행 여부 매핑
            Map<String, Map<String, Integer>> responseMap = new ObjectMapper().readValue(gptResponse, Map.class);

            for (ChecklistItem item : checklistItems) {
                Map<String, Integer> categoryResult = responseMap.getOrDefault(item.getCategory(), new HashMap<>());
                Integer evaluation = categoryResult.getOrDefault(item.getContent(), -1);
                evaluationResult.put(item.getId(), evaluation);
            }
        } catch (Exception e) {
            // 파싱 실패한 경우 모든 항목을 -1로 설정
            for (ChecklistItem item : checklistItems) {
                evaluationResult.put(item.getId(), -1);
            }
        }

        return evaluationResult;
    }
}

