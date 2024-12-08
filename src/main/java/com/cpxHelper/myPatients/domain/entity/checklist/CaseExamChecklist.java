package com.cpxHelper.myPatients.domain.entity.checklist;

import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "caseexam_checklist_tb") // 테이블 이름 지정
public class CaseExamChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_exam_checklist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_exam_id", nullable = false)
    private CaseExam caseExam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_item_id", nullable = false)
    private ChecklistItem checklistItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChecklistStatus status;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public CaseExamChecklist(CaseExam caseExam, ChecklistItem checklistItem, ChecklistStatus status, LocalDateTime updatedAt) {
        this.caseExam = caseExam;
        this.checklistItem = checklistItem;
        this.status = status;
        this.updatedAt = updatedAt;
    }
}
