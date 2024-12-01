package com.cpxHelper.myPatients.domain.entity.chiefcomplaints;

import com.cpxHelper.myPatients.domain.entity.subject.SubjectChiefComplaints;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChiefComplaints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chief_complaints_id", nullable = false)
    private Long id;

    @Column(name = "chief_complaints_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "chiefComplaints", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectChiefComplaints> subjectChiefComplaints;

    @Builder
    public ChiefComplaints(String name) {
        this.name = name;
    }
}
