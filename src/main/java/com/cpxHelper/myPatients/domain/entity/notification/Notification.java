package com.cpxHelper.myPatients.domain.entity.notification;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification_tb") // 테이블 이름 지정
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false, columnDefinition = "boolean default false")
    private boolean isRead; // 알림 읽음 여부

    @Builder
    public Notification(Member member, String message, LocalDateTime createdAt, boolean isRead) {
        this.member = member;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    // 알림 읽음 처리
    public void markAsRead() {
        this.isRead = true;
    }
}

