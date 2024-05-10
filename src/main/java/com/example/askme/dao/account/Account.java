package com.example.askme.dao.account;

import com.example.askme.common.constant.Role;
import com.example.askme.dao.AuditingTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Setter
    @Column(nullable = false, length = 100)
    private String nickname;

    @Setter
    @Column(nullable = false, length = 100)
    private String email;

    @Column
    @ColumnDefault("0")
    private long questionCount = 0;

    public void increaseQuestionCount() {
        this.questionCount++;
    }

    @Setter
    private String imageUrl;

    @Builder
    private Account(String userId, String password, Role role, String nickname, String email, String imageUrl, long questionCount) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.email = email;
        this.imageUrl = imageUrl;
        this.questionCount = questionCount;
    }

    public static Account createUser(String userId, String password, Role role, String nickname, String email, String imageUrl, long questionCount) {
        return new Account(userId, password, role, nickname, email, imageUrl, questionCount);
    }
}
