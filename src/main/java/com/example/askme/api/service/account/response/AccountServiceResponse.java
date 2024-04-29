package com.example.askme.api.service.account.response;

import com.example.askme.domain.account.Account;
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
    private String imageUrl;

    @Builder
    private AccountServiceResponse(Long id, String userId, String nickname, String email, long questionCount, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt, Account createdBy) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.questionCount = questionCount;
        this.imageUrl = imageUrl;

    }

    public static AccountServiceResponse of(Account account) {
        return AccountServiceResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .nickname(account.getNickname())
                .email(account.getEmail())
                .questionCount(account.getQuestionCount())
                .imageUrl(account.getImageUrl())
                .build();
    }
}