package com.cpxHelper.myPatients.domain.entity.chiefcomplaints;

import com.cpxHelper.myPatients.domain.entity.checklist.ChecklistItem;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import com.cpxHelper.myPatients.domain.entity.subject.Subject;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chief_complaints_tb") // 테이블 이름 지정
public class ChiefComplaints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chief_complaints_id", nullable = false)
    private Long id;

    @Column(name = "chief_complaints_name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false)
    private Subject subject;

    @OneToMany(mappedBy = "chiefComplaints", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patients;

    @OneToMany(mappedBy = "chiefComplaints", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> checklistItemList;

    @Builder
    public ChiefComplaints(String name) {
        this.name = name;
    }
}
