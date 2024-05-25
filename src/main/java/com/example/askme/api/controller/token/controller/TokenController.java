package com.example.askme.api.controller.token.controller;

import com.example.askme.api.controller.token.dto.AccessTokenResponseDto;
import com.example.askme.common.ResultResponse;
import com.example.askme.common.jwt.TokenManager;
import com.example.askme.common.resolver.memberInfo.TokenDto;
import com.example.askme.common.resolver.memberInfo.TokenParser;
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
    public ResultResponse<AccessTokenResponseDto> createAccessToken(@TokenParser TokenDto tokenDto) {
        AccessTokenResponseDto accessTokenResponseDto = tokenManager.createAccessTokenByRefreshToken(tokenDto.getToken());
        return ResultResponse.success(accessTokenResponseDto);
    }
}
