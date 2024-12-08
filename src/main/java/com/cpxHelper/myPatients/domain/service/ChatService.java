package com.cpxHelper.myPatients.domain.service;

import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import com.cpxHelper.myPatients.domain.entity.chat.Chat;
import com.cpxHelper.myPatients.domain.repository.ChatRepository;
import com.cpxHelper.myPatients.domain.utils.PromptLoader;
import com.cpxHelper.myPatients.domain.utils.PromptProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    @Lazy
    private final PromptLoader promptLoader;
    private final PromptProcessor promptProcessor;
    private final OpenAIService openAiService;

    // 메시지 저장
    public Chat saveMessage(CaseExam caseExam, String sender, String receiver, String message) {
        Chat chat = Chat.builder()
                .caseExam(caseExam)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        return chatRepository.save(chat);
    }

    // 특정 시험의 전체 메시지 조회
    public List<Chat> getChatsByCaseExam(Long caseExamId) {
        return chatRepository.findByCaseExamIdOrderByCreatedAtAsc(caseExamId);
    }

    // 특정 시간 범위의 메시지 조회
    public List<Chat> getChatsByTimeRange(Long caseExamId, LocalDateTime start, LocalDateTime end) {
        return chatRepository.findByCaseExamIdAndCreatedAtBetween(caseExamId, start, end);
    }

    // 사용자 메시지를 처리하고 GPT 응답을 생성
    public String processUserMessage(CaseExam caseExam, List<Chat> chatHistory, String userMessage) {
        // 사용자 메시지 저장
        saveMessage(caseExam, "USER", "GPT", userMessage);

        // 프롬프트 템플릿 로드
        String promptTemplate = promptLoader.loadPromptTemplate();

        // 환자 정보를 JSON 형식으로 생성
        String patientInfo = getPatientInfoAsJson(caseExam);

        // 메시지 리스트 생성
        List<Map<String, String>> messages = new ArrayList<>();

        // 시스템 메시지 추가
        messages.add(Map.of(
                "role", "system",
                "content", promptTemplate
        ));

        // 환자 정보 추가
        if (patientInfo != null && !patientInfo.isEmpty()) {
            messages.add(Map.of(
                    "role", "assistant",
                    "content", "환자 정보: " + patientInfo
            ));
        }

        // 채팅 내역 추가
        for (Chat chat : chatHistory) {
            messages.add(Map.of(
                    "role", chat.getSenderRole().equals("user") ? "user" : "assistant",
                    "content", chat.getMessage()
            ));
        }

        // 사용자 메시지 추가
        messages.add(Map.of(
                "role", "user",
                "content", userMessage
        ));

        // GPT 호출
        String gptResponse = openAiService.callGptApi(messages);

        // GPT 응답 저장
        saveMessage(caseExam, "GPT", "USER", gptResponse);

        return gptResponse;
    }

    // 환자 정보를 JSON 형식으로 변환하는 메서드 예시
    private String getPatientInfoAsJson(CaseExam caseExam) {
        // 환자 정보를 가져와 JSON 문자열로 변환 (예: Jackson 사용)
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(caseExam.getPatientInfo());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert patient info to JSON", e);
        }
    }

}
