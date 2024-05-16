package com.example.askme.api.service.oauth;

import com.example.askme.api.controller.login.response.OauthLoginResponse;
import com.example.askme.api.service.account.AccountService;
import com.example.askme.api.service.account.request.AccountServiceRequest;
import com.example.askme.common.constant.LoginType;
import com.example.askme.common.jwt.JwtTokenDto;
import com.example.askme.common.jwt.TokenManager;
import com.example.askme.dao.account.Account;
import com.example.askme.dao.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final TokenManager tokenManager;

    public OauthLoginResponse login(String accessToken, LoginType loginType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(loginType);
        AccountServiceRequest accountServiceRequest = socialLoginApiService.getAccountInfo(accessToken);
        log.info("userInfo: {}", accountServiceRequest);

        Optional<Account> accountOptional = accountService.findByUserNickname(accountServiceRequest.getNickname());

        JwtTokenDto jwtTokenDto;

        if (accountOptional.isEmpty()) {
            Account newAccount = accountServiceRequest.toEntity();
            jwtTokenDto = tokenManager.createJwtTokenDto(newAccount.getId(), newAccount.getRole());
            newAccount.updateRefreshToken(jwtTokenDto);
            accountRepository.save(newAccount);
        } else {
            Account existingAccount = accountOptional.get();
            jwtTokenDto = tokenManager.createJwtTokenDto(existingAccount.getId(), existingAccount.getRole());
            existingAccount.updateRefreshToken(jwtTokenDto);
            accountRepository.save(existingAccount);
        }

        return OauthLoginResponse.of(jwtTokenDto);
    }
}
