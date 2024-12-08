package com.cpxHelper.myPatients.domain.entity.mockexam;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.subject.Subject;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "mockexam_tb") // 테이블 이름 지정
public class MockExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mock_exam_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 시험 응시자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject; // 관련 과목

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "mockExam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MockExamQuestion> questions;

    @Builder
    public MockExam(Member member, Subject subject, LocalDateTime createdAt) {
        this.member = member;
        this.subject = subject;
        this.createdAt = createdAt;
    }
}
