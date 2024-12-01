package com.cpxHelper.myPatients.domain.entity.member;

import com.cpxHelper.myPatients.domain.entity.comment.Comment;
import com.cpxHelper.myPatients.domain.entity.profile.Profile;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id", nullable = false)
    private Long id;

    @OneToOne(mappedBy = "member")
    private Profile profile;

    @Column(name="member_email",nullable = false, unique = true)
    private String email;

    @Column(name="member_password", nullable = false)
    private String password;

    @Column(name="member_phone", nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public void updateIsDeleted() {
        this.isDeleted = !this.isDeleted;
    }

    @Builder
    public Member(String email, String password, MemberRole memberRole){
        this.email = email;
        this.password = password;
        this.memberRole = Objects.requireNonNullElse(this.memberRole, this.memberRole.ROLE_USER); //값이 없다면, ROLE_USER로 초기화
    }
}
