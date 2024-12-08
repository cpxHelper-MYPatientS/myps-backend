package com.cpxHelper.myPatients.domain.controller;

import com.cpxHelper.myPatients.domain.dto.ChatRequestDto;
import com.cpxHelper.myPatients.domain.dto.ChatResponseDto;
import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import com.cpxHelper.myPatients.domain.entity.chat.Chat;
import com.cpxHelper.myPatients.domain.service.CaseExamService;
import com.cpxHelper.myPatients.domain.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final CaseExamService caseExamService;

    // 메시지 저장
    @PostMapping
    public ResponseEntity<Chat> saveChat(
            @RequestParam Long caseExamId,
            @RequestParam String sender,
            @RequestParam String receiver,
            @RequestParam String message) {
        CaseExam caseExam = caseExamService.getCaseExamById(caseExamId);
        Chat chat = chatService.saveMessage(caseExam, sender, receiver, message);
        return ResponseEntity.ok(chat);
    }

    // 특정 시험의 메시지 조회
    @GetMapping("/{caseExamId}")
    public ResponseEntity<List<Chat>> getChats(@PathVariable Long caseExamId) {
        List<Chat> chats = chatService.getChatsByCaseExam(caseExamId);
        return ResponseEntity.ok(chats);
    }

    @PostMapping("/message")
    public ResponseEntity<ChatResponseDto> handleChat(@RequestBody ChatRequestDto requestDto) {
        CaseExam caseExam;

        // 요청에 caseExamId가 있으면 해당 케이스 시험 조회
        if (requestDto.getCaseExamId() != null) {
            caseExam = caseExamService.getCaseExamById(requestDto.getCaseExamId());
            if (caseExam == null) {
                throw new IllegalArgumentException("Invalid CaseExam ID: " + requestDto.getCaseExamId());
            }
        } else {
            caseExam = caseExamService.createCaseExam(requestDto.getMemberId(), requestDto.getPatientId());
        }

        // 현재 진행 중인 채팅 내역 조회
        List<Chat> chatHistory = chatService.getChatsByCaseExam(caseExam.getId());

        // 사용자 메시지를 처리하고 GPT 응답 생성
        String gptResponse = chatService.processUserMessage(caseExam, chatHistory, requestDto.getMessage());

        // GPT 응답을 채팅 내역에 추가(저장 내용이랑 다른 부분이 있어 추후 수정이 필요할 듯.)
        Chat gptChat = Chat.builder()
                .caseExam(caseExam)
                .sender("GPT")
                .receiver("USER")
                .message(gptResponse)
                .createdAt(LocalDateTime.now())
                .build();
        chatHistory.add(gptChat); // 채팅 내역에 GPT 응답 추가

        // 채팅 내역을 DTO로 변환
        List<ChatResponseDto.MessageDto> messages = chatHistory.stream()
                .map(chat -> ChatResponseDto.MessageDto.builder()
                        .sender(chat.getSender())
                        .content(chat.getMessage())
                        .build())
                .collect(Collectors.toList());

        // 응답 생성
        ChatResponseDto response = ChatResponseDto.builder()
                .caseExamId(caseExam.getId())
                .messages(messages)
                .build();

        return ResponseEntity.ok(response);
    }


}
