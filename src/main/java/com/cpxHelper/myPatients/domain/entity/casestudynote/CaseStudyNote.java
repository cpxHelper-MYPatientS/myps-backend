package com.cpxHelper.myPatients.domain.entity.casestudynote;

import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "casestudynote_tb") // 테이블 이름 지정
public class CaseStudyNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_exam_id", nullable = false)
    private CaseExam caseExam; // 연결된 사례 시험

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 정리 노트 내용

    @Column(nullable = false)
    private LocalDateTime createdAt; // 노트 생성 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 노트 마지막 수정 시간

    @Builder
    public CaseStudyNote(CaseExam caseExam, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.caseExam = caseExam;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 노트 내용 업데이트
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
