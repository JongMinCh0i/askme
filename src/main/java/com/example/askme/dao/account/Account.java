package com.example.askme.dao.account;

import com.example.askme.common.constant.LoginType;
import com.example.askme.common.constant.Role;
import com.example.askme.common.jwt.JwtTokenDto;
import com.example.askme.common.util.DateTimeUtils;
import com.example.askme.dao.AuditingTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(length = 250)
    private String refreshToken;

    @Column
    @ColumnDefault("0")
    private long questionCount = 0;

    public void increaseQuestionCount() {
        this.questionCount++;
    }

    private LocalDateTime tokenExpireTime;

    @Setter
    private String imageUrl;

    @Builder
    private Account(String userId, String password, Role role, String nickname, String email, String imageUrl, LoginType loginType, long questionCount) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.email = email;
        this.imageUrl = imageUrl;
        this.loginType = loginType;
        this.questionCount = questionCount;
    }

    public static Account createUser(String userId, String password, Role role, String nickname, String email, String imageUrl, LoginType loginType, long questionCount) {
        return new Account(userId, password, role, nickname, email, imageUrl, loginType, questionCount);
    }

    public static Account createUserByOauth(String nickname, String email, String imageUrl, LoginType loginType, long questionCount) {
        return new Account("Oauth", "Oauth", Role.QUESTIONER, nickname, email, imageUrl, loginType, questionCount);
    }

    public void updateRefreshToken(JwtTokenDto jwtTokenDto) {
        this.refreshToken = jwtTokenDto.getRefreshToken();
        this.tokenExpireTime = DateTimeUtils.convertToLocalDateTime(jwtTokenDto.getRefreshTokenExpiresTime());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpireTime = now;
    }
}
