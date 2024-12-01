package com.cpxHelper.myPatients.domain.entity.member;

import com.cpxHelper.myPatients.domain.entity.subject.Subject;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_subject_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false)
    private Subject subject;

    @Builder
    public MemberSubject(Member member, Subject subject) {
        this.member = member;
        this.subject = subject;
    }
}

