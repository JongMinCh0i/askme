package com.example.askme.api.service.login.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginServiceRequest {

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("redirect_uri")
    private String redirectUri;

    @JsonProperty("code")
    private String code;

    @JsonProperty("client_secret")
    private String clientSecret;

    @Builder
    public LoginServiceRequest(String grantType, String clientId, String redirectUri, String code, String clientSecret) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
        this.clientSecret = clientSecret;
    }
}
