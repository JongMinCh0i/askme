package com.example.askme.api.controller.login.request;

import com.example.askme.api.service.login.request.LoginServiceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginCreateRequest {

    @NotEmpty(message = "grantType 은 필수 입니다.")
    @JsonProperty("grant_type")
    private String grantType;

    @NotEmpty(message = "clientId 는 필수 입니다.")
    @JsonProperty("client_id")
    private String clientId;

    @NotEmpty(message = "redirect_uri 는 필수 입니다.")
    @JsonProperty("redirect_uri")
    private String redirectUri;

    @NotEmpty
    @NotEmpty(message = "code 는 필수 입니다.")
    @JsonProperty("code")
    private String code;

    @NotEmpty(message = "client_secret 는 필수 입니다.")
    @JsonProperty("client_secret")
    private String clientSecret;

    @Builder
    private LoginCreateRequest(String grantType, String clientId, String redirectUri, String code, String clientSecret) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
        this.clientSecret = clientSecret;
    }

    public LoginServiceRequest toServiceRequest() {
        return LoginServiceRequest.builder()
                .grantType(grantType)
                .clientId(clientId)
                .redirectUri(redirectUri)
                .code(code)
                .clientSecret(clientSecret)
                .build();
    }
}
