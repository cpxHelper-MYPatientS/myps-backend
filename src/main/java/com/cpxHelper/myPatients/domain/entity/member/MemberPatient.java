package com.cpxHelper.myPatients.domain.entity.member;

import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_patient_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    private Patient patient;

    @Builder
    public MemberPatient(Member member, Patient patient) {
        this.member = member;
        this.patient = patient;
    }
}