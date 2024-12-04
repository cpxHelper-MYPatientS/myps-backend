package com.cpxHelper.myPatients.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatResponseDto {

    private Long caseExamId; // CaseExam ID
    private List<MessageDto> messages; // 메시지 리스트

    @Getter
    @Builder
    public static class MessageDto {
        private String sender; // 발신자
        private String content; // 메시지 내용
    }
}