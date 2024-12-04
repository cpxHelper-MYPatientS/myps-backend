package com.cpxHelper.myPatients.domain.service;

import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import com.cpxHelper.myPatients.domain.entity.chat.Chat;
import com.cpxHelper.myPatients.domain.repository.ChatRepository;
import com.cpxHelper.myPatients.domain.utils.PromptLoader;
import com.cpxHelper.myPatients.domain.utils.PromptProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public String processUserMessage(CaseExam caseExam, String userMessage) {
        // 사용자 메시지 저장
        saveMessage(caseExam, "USER", "GPT", userMessage);

        // 프롬프트 템플릿 로드
        String promptTemplate = promptLoader.loadPromptTemplate();

        // 프롬프트 치환 데이터 생성
        Map<String, String> variables = Map.of(
                "case_exam_id", String.valueOf(caseExam.getId()),
                "user_message", userMessage
        );

        // 치환된 프롬프트 생성
        String gptPrompt = promptProcessor.processTemplate(promptTemplate, variables);

        // GPT 호출
        String gptResponse = openAiService.callGptApi(gptPrompt);

        // GPT 응답 저장
        saveMessage(caseExam, "GPT", "USER", gptResponse);

        return gptResponse;
    }

}
