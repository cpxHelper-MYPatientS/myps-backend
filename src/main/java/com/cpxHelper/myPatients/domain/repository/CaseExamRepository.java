package com.cpxHelper.myPatients.domain.repository;

import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaseExamRepository extends JpaRepository<CaseExam, Long> {

    // 특정 작성자(Member)와 관련된 모든 사례 시험 조회
    List<CaseExam> findByMemberId(Long memberId);

    // 특정 환자(Patient)와 관련된 모든 사례 시험 조회
    List<CaseExam> findByPatientId(Long patientId);

    // 특정 작성자와 환자가 연결된 사례 시험 조회
    List<CaseExam> findByMemberIdAndPatientId(Long memberId, Long patientId);

    // 특정 시간 범위 내에 생성된 사례 시험 조회
    List<CaseExam> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // JPQL로 특정 조건에 맞는 사례 시험 조회 (예: 환자 이름으로 검색)
    @Query("SELECT ce FROM CaseExam ce WHERE ce.patient.id = :patientId AND ce.createdAt >= :start AND ce.createdAt <= :end")
    List<CaseExam> findCaseExamsForPatientWithinDateRange(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
