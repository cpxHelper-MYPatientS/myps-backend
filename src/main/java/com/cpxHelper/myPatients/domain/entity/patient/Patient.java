package com.cpxHelper.myPatients.domain.entity.patient;

import com.cpxHelper.myPatients.domain.entity.chiefcomplaints.ChiefComplaints;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "patient_tb") // 테이블 이름 지정
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_complaints_id", nullable = false)
    private ChiefComplaints chiefComplaints;

    @Builder
    public Patient(String name, LocalDate dateOfBirth, Integer age, String gender, ChiefComplaints chiefComplaints) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
        this.chiefComplaints = chiefComplaints;
    }
}
