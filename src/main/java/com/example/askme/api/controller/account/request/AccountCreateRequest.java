package com.example.askme.api.controller.account.request;

import com.example.askme.api.service.account.request.AccountServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {

    @NotEmpty(message = "닉네임은 필수입니다.")
    @Size(max = 50 , message = "닉네임은 50자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "로그인 Id는 필수입니다.")
    @Size(max = 50, message = "로그인 Id는 50자 이하로 입력해주세요.")
    private String userId;

    @Size(max = 50, message = "이메일은 50자 이하로 입력해주세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수입니다.")
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
