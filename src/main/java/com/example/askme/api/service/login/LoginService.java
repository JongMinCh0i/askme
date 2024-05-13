package com.example.askme.api.service.login;

import com.example.askme.api.controller.login.request.LoginCreateRequest;
import com.example.askme.api.service.login.request.LoginServiceRequest;
import com.example.askme.common.http.AskMeHttpClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private final AskMeHttpClient askMeHttpClient;

    public LoginService(AskMeHttpClient askMeHttpClient) {
        this.askMeHttpClient = askMeHttpClient;
    }

    public String requestToken(String ContentType, LoginCreateRequest request) {
        LoginServiceRequest serviceRequest = request.toServiceRequest();

        //uri
        String uri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", serviceRequest.getGrantType())
                .queryParam("client_id", serviceRequest.getClientId())
                .queryParam("redirect_uri", serviceRequest.getRedirectUri())
                .queryParam("code", serviceRequest.getCode())
                .queryParam("client_secret", serviceRequest.getClientSecret())
                .build()
                .toUriString();

        //header
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", ContentType);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        //call
        return askMeHttpClient.getData(uri, HttpMethod.POST, httpHeaders);
    }

}
