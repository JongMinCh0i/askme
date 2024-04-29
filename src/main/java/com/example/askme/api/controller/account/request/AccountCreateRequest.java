package com.example.askme.api.controller.account.request;

import com.example.askme.api.service.account.request.AccountServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountCreateRequest {

    @NotNull
    @Size(max = 50)
    private String nickname;

    @NotNull
    @Size(max = 50)
    private String userId;

    @Email
    @Size(max = 50)
    @NotNull
    private String email;

    @NotNull
    private String password;

    private String imageUrl;

    public AccountServiceRequest toServiceRequest() {
        return AccountServiceRequest.builder()
                .nickname(nickname)
                .userId(userId)
                .email(email)
                .password(password)
                .imageUrl(imageUrl)
                .build();
    }
}
