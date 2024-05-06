package com.example.askme.dao.account;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String password;

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
    private Account(String userId, String password, String nickname, String email, String imageUrl, long questionCount) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl = imageUrl;
        this.questionCount = questionCount;
    }

    public static Account createUser(String userId, String password, String nickname, String email, String imageUrl, long questionCount) {
        return new Account(userId, password, nickname, email, imageUrl, questionCount);
    }
}