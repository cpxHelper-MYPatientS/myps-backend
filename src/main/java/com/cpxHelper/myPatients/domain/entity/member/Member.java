package com.cpxHelper.myPatients.domain.entity.member;

import com.cpxHelper.myPatients.domain.entity.comment.Comment;
import com.cpxHelper.myPatients.domain.entity.profile.Profile;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor // JPA에서 필요한 기본 생성자
@Entity
@Table(name = "member_tb") // 테이블 이름 지정
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @OneToOne(mappedBy = "member")
    private Profile profile;

    @Column(name = "member_email", nullable = false, unique = true)
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Column(name = "member_phone", nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // isDeleted 필드 업데이트 메서드
    public void updateIsDeleted() {
        this.isDeleted = !this.isDeleted;
    }

    // 생성자 - @Builder 패턴
    @Builder
    public Member(String email, String password, String phone, MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.memberRole = memberRole != null ? memberRole : MemberRole.ROLE_USER; // 기본값 ROLE_USER 설정
    }
}