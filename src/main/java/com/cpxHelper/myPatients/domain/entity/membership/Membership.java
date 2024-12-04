package com.cpxHelper.myPatients.domain.entity.membership;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor // 기본 생성자 추가
@Entity
@Table(name = "membership")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_type", nullable = false)
    private MembershipType membershipType;

    @Column(name = "membership_activated_date", nullable = false)
    private LocalDateTime activatedDate;

    @Column(name = "membership_deactivated_date", nullable = false)
    private LocalDateTime deactivatedDate;

    @Column(name = "remaining_mock_exams", nullable = false)
    private int remainingMockExams;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default false")
    private boolean isActive; // 멤버십 활성화 여부

    @Builder
    public Membership(Member member, MembershipType membershipType, LocalDateTime activatedDate, boolean isActive) {
        this.member = member;
        this.membershipType = membershipType;
        this.activatedDate = activatedDate;
        this.remainingMockExams = membershipType.getMockExamLimit(); // 초기값: 모의시험 이용 가능 횟수
        this.isActive = isActive;
    }

    // 모의시험 사용 시 호출
    public void useMockExam() {
        if (!isActive) {
            throw new IllegalStateException("Membership is not active.");
        }
        if (remainingMockExams <= 0) {
            throw new IllegalStateException("No remaining mock exams available.");
        }
        remainingMockExams--;
    }

    // 멤버십 활성화
    public void activateMembership() {
        this.isActive = true;
    }

    // 멤버십 비활성화
    public void deactivateMembership() {
        this.isActive = false;
    }

    // 멤버십 재충전 시 호출
    public void rechargeMockExams() {
        if (!isActive) {
            throw new IllegalStateException("Cannot recharge an inactive membership.");
        }
        this.remainingMockExams = membershipType.getMockExamLimit();
    }
}
