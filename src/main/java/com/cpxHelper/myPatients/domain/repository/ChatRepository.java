package com.cpxHelper.myPatients.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cpxHelper.myPatients.domain.entity.chat.Chat;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    // 특정 시험의 대화 조회
    List<Chat> findByCaseExamIdOrderByCreatedAtAsc(Long caseExamId);

    // 시간 범위 내의 대화 조회
    List<Chat> findByCaseExamIdAndCreatedAtBetween(Long caseExamId, LocalDateTime start, LocalDateTime end);
}
