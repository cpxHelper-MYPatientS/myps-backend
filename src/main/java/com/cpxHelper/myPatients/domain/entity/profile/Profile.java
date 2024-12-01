package com.cpxHelper.myPatients.domain.entity.profile;

import com.cpxHelper.myPatients.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @Column(name = "profile_nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_name", nullable = false)
    private String name;

    @Column(name = "profile_grade")
    private String grade;

    @Column(name = "profile_school", nullable = false)
    private String school;

    @Column(name="profile_hospital")
    private String hospital;

    @Column(name = "profile_exam_date")
    private LocalDateTime examDate;

    @Builder
    public Profile(Member member, String school, String hospital, String nickname, String grade, String name, LocalDateTime examDate) {
        this.member = member;
        this.school = school;
        this.hospital = hospital;
        this.nickname = nickname;
        this.grade = grade;
        this.name = name;
        this.examDate = examDate;
    }
}
