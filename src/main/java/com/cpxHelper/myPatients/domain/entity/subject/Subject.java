package com.cpxHelper.myPatients.domain.entity.subject;

import com.cpxHelper.myPatients.domain.entity.chiefcomplaints.ChiefComplaints;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "subject_tb") // 테이블 이름 지정
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id", nullable = false)
    private Long id;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiefComplaints> chiefComplaints;

    @Builder
    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }
}
