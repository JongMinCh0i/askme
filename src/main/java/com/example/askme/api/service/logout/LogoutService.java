package com.example.askme.api.service.logout;

import com.example.askme.api.service.account.AccountService;
import com.example.askme.common.error.ErrorCode;
import com.example.askme.common.error.exception.BusinessException;
import com.example.askme.common.jwt.TokenManager;
import com.example.askme.common.jwt.constatnt.TokenType;
import com.example.askme.dao.account.Account;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutService {

    private final AccountService accountService;
    private final TokenManager tokenManager;

    public void logout(String accessToken) {

        // 1. 토큰 검증
        tokenManager.validateToken(accessToken);

        // 2. 토큰 타입 확인
        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String subject = tokenClaims.getSubject();

        if(!TokenType.isAccessToken(subject)) {
            throw new BusinessException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 3.refresh token 만료 처리
        Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        Account account = accountService.findAccountByAccountId(memberId);
        account.expireRefreshToken(LocalDateTime.now());

    }
}
