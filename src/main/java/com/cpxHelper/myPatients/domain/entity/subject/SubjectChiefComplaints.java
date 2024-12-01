package com.cpxHelper.myPatients.domain.entity.subject;

import com.cpxHelper.myPatients.domain.entity.chiefcomplaints.ChiefComplaints;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SubjectChiefComplaints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_chief_complaints_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_complaints_id", referencedColumnName = "chief_complaints_id", nullable = false)
    private ChiefComplaints chiefComplaints;

    @Builder
    public SubjectChiefComplaints(Subject subject, ChiefComplaints chiefComplaints) {
        this.subject = subject;
        this.chiefComplaints = chiefComplaints;
    }
}

