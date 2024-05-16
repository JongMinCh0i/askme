package com.example.askme.api.service.account;

import com.example.askme.api.service.account.response.AccountServiceResponse;
import com.example.askme.dao.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountInfoService {

    private final AccountService accountService;

    @Transactional(readOnly = true)
    public AccountServiceResponse getAccountInfo(Long memberId) {
        Account account = accountService.findAccountByAccountId(memberId);
        return AccountServiceResponse.of(account);
    }
}
