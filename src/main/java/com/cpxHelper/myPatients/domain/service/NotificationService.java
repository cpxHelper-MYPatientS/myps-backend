package com.cpxHelper.myPatients.domain.service;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.notification.Notification;
import com.cpxHelper.myPatients.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 생성
    public Notification createNotification(Member member, String message) {
        Notification notification = Notification.builder()
                .member(member)
                .message(message)
                .createdAt(LocalDateTime.now())
                .isRead(false) // 초기값: 읽지 않음
                .build();
        return notificationRepository.save(notification);
    }

    // 특정 멤버의 읽지 않은 알림 조회
    public List<Notification> getUnreadNotifications(Long memberId) {
        return notificationRepository.findByMemberIdAndIsReadFalse(memberId);
    }

    // 알림 읽음 처리
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + notificationId));
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
