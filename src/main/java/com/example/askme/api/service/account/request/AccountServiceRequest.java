package com.example.askme.api.service.account.request;

import com.example.askme.domain.account.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountServiceRequest {

    private String nickname;
    private String userId;
    private String email;
    private String password;
    private String imageUrl;
    private long questionCount;

    @Builder
    private AccountServiceRequest(String nickname, String userId, String email, String password, String imageUrl) {
        this.nickname = nickname;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public Account toEntity() {
        return Account.createUser(
                userId,
                password,
                nickname,
                email,
                imageUrl,
                questionCount
        );
    }
}
