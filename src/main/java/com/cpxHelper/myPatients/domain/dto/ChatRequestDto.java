package com.cpxHelper.myPatients.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDto {
    private Long caseExamId; // 시험 ID
    private Long patientId;  // 환자 ID
    private String message;  // 사용자 메시지
}