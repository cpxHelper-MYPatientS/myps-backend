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
        ), "checklist");
        System.out.println(gptResponse);

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
            // 앞뒤의 ```json 태그 제거
            String cleanedResponse = cleanJsonString(gptResponse);

            // GPT 응답(JSON)을 파싱하여 체크리스트 항목과 수행 여부 매핑
            Map<String, List<Map<String, Object>>> responseMap = new ObjectMapper().readValue(cleanedResponse, Map.class);

            for (ChecklistItem item : checklistItems) {
                // 카테고리 데이터 가져오기
                List<Map<String, Object>> categoryResult = responseMap.getOrDefault(item.getCategory(), new ArrayList<>());

                // 해당 항목의 평가 값 찾기
                Integer evaluation = -1;
                for (Map<String, Object> result : categoryResult) {
                    String content = (String) result.getOrDefault("항목", "");
                    System.out.println("Content from JSON: " + content);
                    System.out.println("Item Content: " + item.getContent());

                    if (content.trim().equalsIgnoreCase(item.getContent().trim())) {
                        // "평가" 값을 가져와 확인
                        Object evaluationRaw = result.get("수행");
                        System.out.println("Raw Evaluation: " + evaluationRaw + ", Type: " + (evaluationRaw != null ? evaluationRaw.getClass() : "null"));

                        // 타입 확인 후 처리
                        if (evaluationRaw instanceof Integer) {
                            evaluation = (Integer) evaluationRaw;
                        } else if (evaluationRaw instanceof String) {
                            try {
                                evaluation = Integer.parseInt((String) evaluationRaw);
                            } catch (NumberFormatException e) {
                                System.out.println("Failed to parse evaluation: " + evaluationRaw);
                            }
                        }
                        break;
                    }
                }
                System.out.println("Evaluation for item " + item.getContent() + ": " + evaluation);
                evaluationResult.put(item.getId(), evaluation);
            }
        } catch (Exception e) {
            System.out.println("Exception occurred > 모든 항목을 -1로 설정합니다.");
            // 파싱 실패한 경우 모든 항목을 -1로 설정
            for (ChecklistItem item : checklistItems) {
                evaluationResult.put(item.getId(), -1);
            }
        }

        return evaluationResult;
    }

    // ```json 태그와 그 외 불필요한 텍스트 제거
    private String cleanJsonString(String rawJson) {
        if (rawJson.startsWith("```json")) {
            rawJson = rawJson.substring(7); // ```json 제거
        }
        if (rawJson.endsWith("```")) {
            rawJson = rawJson.substring(0, rawJson.length() - 3); // ``` 제거
        }
        return rawJson.trim(); // 앞뒤 공백 제거
    }
}

