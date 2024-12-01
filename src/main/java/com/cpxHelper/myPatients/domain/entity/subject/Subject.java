package com.cpxHelper.myPatients.domain.entity.subject;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id", nullable = false)
    private Long id;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectChiefComplaints> subjectChiefComplaints;

    @Builder
    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }
}
