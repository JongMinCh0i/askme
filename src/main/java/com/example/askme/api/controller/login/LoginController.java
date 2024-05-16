package com.example.askme.api.controller.login;

import com.example.askme.api.controller.login.request.LoginCreateRequest;
import com.example.askme.api.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/oauth/kakao/callback")
    public @ResponseBody String loginCallback(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=UTF-8";
        LoginCreateRequest request = LoginCreateRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("authorization_code")
                .code(code)
                .redirectUri("http://localhost:8080/oauth/kakao/callback")
                .build();

        return "kakao token: " + loginService.requestToken(contentType, request);
    }
}
