package com.example.askme.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String userPassword;

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

    private UserAccount(String userId, String nickname, String email, int questionCount, String imageUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.questionCount = questionCount;
        this.imageUrl = imageUrl;
    }

    public static UserAccount createUser(String userId, String nickname, String email, int questionCount, String imageUrl) {
        return new UserAccount(userId, nickname, email, questionCount, imageUrl);
    }
}
