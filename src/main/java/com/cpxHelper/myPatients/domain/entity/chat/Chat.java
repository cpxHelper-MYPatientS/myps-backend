package com.cpxHelper.myPatients.domain.entity.chat;

import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_tb") // 테이블 이름 지정
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_exam_id", nullable = false)
    private CaseExam caseExam; // 연결된 시험 정보

    @Column(nullable = false)
    private String sender; // 송신자 (GPT 또는 USER)

    @Column(nullable = false)
    private String receiver; // 수신자 (USER 또는 GPT)

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message; // 대화 메시지 내용

    @Column(nullable = false)
    private LocalDateTime createdAt; // 메시지 생성 시간

    @Builder
    public Chat(CaseExam caseExam, String sender, String receiver, String message, LocalDateTime createdAt) {
        this.caseExam = caseExam;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = createdAt;
    }
}
