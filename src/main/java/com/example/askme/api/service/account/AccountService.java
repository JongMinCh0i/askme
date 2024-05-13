package com.example.askme.api.service.account;

import com.example.askme.api.service.account.request.AccountServiceRequest;
import com.example.askme.api.service.account.response.AccountServiceResponse;
import com.example.askme.common.error.ErrorCode;
import com.example.askme.common.error.exception.BusinessException;
import com.example.askme.dao.account.Account;
import com.example.askme.dao.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountServiceResponse signUp(AccountServiceRequest accountServiceRequest) {
        Account account = accountRepository.save(accountServiceRequest.toEntity());
        return AccountServiceResponse.of(account);
    }

    public Account findByRefreshToken(String refreshToken) {
        Account account = accountRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_REFRESH_TOKEN));

        LocalDateTime tokenExpireTime = account.getTokenExpireTime();
        if (LocalDateTime.now().isAfter(tokenExpireTime)) {
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        return account;
    }

}
