package com.example.askme.api.service.account.request;

import com.example.askme.common.constant.LoginType;
import com.example.askme.dao.account.Account;
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
    private LoginType loginType;
    private long questionCount;

    @Builder
    public AccountServiceRequest(String nickname, String userId, String email, String password, String imageUrl, LoginType loginType, long questionCount) {
        this.nickname = nickname;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.loginType = loginType;
        this.questionCount = questionCount;
    }

    public Account toEntity() {
        return Account.createUserByOauth(
                this.nickname,
                "",
                this.imageUrl,
                this.loginType,
                0
        );
    }
}
