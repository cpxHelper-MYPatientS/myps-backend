package com.cpxHelper.myPatients.domain.repository;

import com.cpxHelper.myPatients.domain.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 멤버의 알림 조회
    List<Notification> findByMemberIdAndIsReadFalse(Long memberId);
}
