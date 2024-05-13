package com.example.askme.api.service.token.serivce;

import com.example.askme.api.controller.token.dto.AccessTokenResponseDto;
import com.example.askme.api.service.account.AccountService;
import com.example.askme.common.jwt.TokenManager;
import com.example.askme.common.jwt.constatnt.GrantType;
import com.example.askme.dao.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final AccountService accountService;
    private final TokenManager tokenManager;

    public AccessTokenResponseDto createAccessTokenByRefreshToken(String refreshToken) {
        Account account = accountService.findByRefreshToken(refreshToken);

        Date accessTokenExpireTime = tokenManager.createAccessTokenExpireTime();
        String accessToken = tokenManager.createAccessToken(account.getId(), account.getRole(), accessTokenExpireTime);

        return AccessTokenResponseDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .build();
    }
}
