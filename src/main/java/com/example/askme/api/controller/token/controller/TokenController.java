package com.example.askme.api.controller.token.controller;

import com.example.askme.api.controller.token.dto.AccessTokenResponseDto;
import com.example.askme.common.ResultResponse;
import com.example.askme.common.jwt.TokenManager;
import com.example.askme.common.util.AuthorizationHeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final TokenManager tokenManager;

    @PostMapping("/access-token/issue")
    public ResultResponse<AccessTokenResponseDto> createAccessToken(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorizationHeader(authorizationHeader);

        String refreshToken = authorizationHeader.split(" ")[1];
        AccessTokenResponseDto accessTokenResponseDto = tokenManager.createAccessTokenByRefreshToken(refreshToken);
        return ResultResponse.success(accessTokenResponseDto);
    }
}
