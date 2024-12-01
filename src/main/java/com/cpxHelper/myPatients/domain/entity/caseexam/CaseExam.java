package com.cpxHelper.myPatients.domain.entity.caseexam;

import com.cpxHelper.myPatients.domain.entity.checklist.CaseExamChecklist;
import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class CaseExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_exam_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient; // 관련 환자

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "caseExam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseExamChecklist> checklists;

    @Builder
    public CaseExam(Member member, Patient patient, LocalDateTime createdAt) {
        this.member = member;
        this.patient = patient;
        this.createdAt = createdAt;
    }
}
