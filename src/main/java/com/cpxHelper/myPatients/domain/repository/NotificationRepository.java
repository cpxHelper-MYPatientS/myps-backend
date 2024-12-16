package com.cpxHelper.myPatients.domain.repository;

import com.cpxHelper.myPatients.domain.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 멤버의 알림 조회
    List<Notification> findByMemberIdAndIsReadFalse(Long memberId);
}
