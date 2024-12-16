package com.cpxHelper.myPatients.domain.entity.checklist;

import com.cpxHelper.myPatients.domain.entity.chiefcomplaints.ChiefComplaints;
import com.cpxHelper.myPatients.domain.entity.subject.Subject;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "checklist_item_tb") // 테이블 이름 지정
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chief_complaints_id", referencedColumnName = "chief_complaints_id", nullable = false)
    private ChiefComplaints chiefComplaints;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isActive;

    @Builder
    public ChecklistItem(String content, String category, boolean isActive) {
        this.content = content;
        this.category = category;
        this.isActive = isActive;
    }
}
