package com.example.askme.api.service.account.response;

import com.example.askme.common.constant.Role;
import com.example.askme.dao.account.Account;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AccountServiceResponse {

    private Long id;
    private String userId;
    private String nickname;
    private String email;
    private long questionCount;
    private Role role;
    private String imageUrl;

    @Builder
    private AccountServiceResponse(Long id, String userId, String nickname, Role role, String email, long questionCount, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt, Account createdBy) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.questionCount = questionCount;
        this.imageUrl = imageUrl;
    }

    public static AccountServiceResponse of(Account account) {
        return AccountServiceResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .nickname(account.getNickname())
                .email(account.getEmail())
                .role(account.getRole())
                .questionCount(account.getQuestionCount())
                .imageUrl(account.getImageUrl())
                .build();
    }
}
