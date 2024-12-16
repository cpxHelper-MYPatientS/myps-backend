package com.cpxHelper.myPatients.domain.repository;

import com.cpxHelper.myPatients.domain.entity.checklist.CaseExamChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseExamChecklistRepository extends JpaRepository<CaseExamChecklist, Long> {
}

