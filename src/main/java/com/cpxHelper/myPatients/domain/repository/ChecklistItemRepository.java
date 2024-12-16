package com.cpxHelper.myPatients.domain.repository;

import com.cpxHelper.myPatients.domain.entity.checklist.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

    @Query("SELECT ci FROM ChecklistItem ci " +
            "WHERE ci.chiefComplaints.id = :chiefComplaintsId " +
            "AND ci.isActive = true " +
            "ORDER BY ci.category")
    List<ChecklistItem> findActiveItemsByChiefComplaintsId(@Param("chiefComplaintsId") Long chiefComplaintsId);
}
