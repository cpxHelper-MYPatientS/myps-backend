package com.cpxHelper.myPatients.domain.entity.checklist;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean isActive;

    @Builder
    public ChecklistItem(String name, String category, boolean isActive) {
        this.name = name;
        this.category = category;
        this.isActive = isActive;
    }
}
