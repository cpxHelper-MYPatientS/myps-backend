package com.cpxHelper.myPatients.domain.entity.caseexam;

import com.cpxHelper.myPatients.domain.entity.checklist.CaseExamChecklist;
import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "caseexam_tb") // 테이블 이름 지정
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
    public CaseExam(Long id, Member member, Patient patient, LocalDateTime createdAt) {
        this.id = id; // 테스트 코드를 위한 빌더 패턴
        this.member = member;
        this.patient = patient;
        this.createdAt = createdAt;
    }

    /**
     * 환자 정보를 JSON 형식으로 반환.
     * @return Map<String, Object> 환자 정보
     */
    public Map<String, Object> getPatientInfo() {
        if (patient == null) {
            return Map.of(); // 환자 정보가 없는 경우 빈 Map 반환
        }

        Map<String, Object> patientInfo = new HashMap<>();
        patientInfo.put("name", patient.getName());
        // patientInfo.put("dateOfBirth", patient.getDateOfBirth());
        patientInfo.put("age", patient.getAge());
        patientInfo.put("gender", patient.getGender());
        patientInfo.put("chiefComplaints", patient.getChiefComplaints().getName()); // ChiefComplaints 설명 추가

        return patientInfo;
    }
}
