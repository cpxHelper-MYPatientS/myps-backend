package com.cpxHelper.myPatients.domain.entity.patient;

import com.cpxHelper.myPatients.domain.entity.subject.Subject;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Long id;

    @Column(name = "chief_complaints", nullable = false)
    private String chiefComplaints;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false)
//    private Subject subject;

    @Builder
    public Patient(String chiefComplaints, Subject subject) {
        this.chiefComplaints = chiefComplaints;
//        this.subject = subject;
    }
}
