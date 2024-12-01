package com.cpxHelper.myPatients.domain.entity.comment;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT") // 긴 문자열 저장
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    private Patient patient;
}
