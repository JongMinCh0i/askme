package com.example.askme.api.service.account;

import com.example.askme.api.controller.account.request.AccountCreateRequest;
import com.example.askme.api.service.account.response.AccountServiceResponse;
import com.example.askme.dao.account.Account;
import com.example.askme.dao.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountServiceResponse signUp(AccountCreateRequest createRequestAccount) {
        Account account = accountRepository.save(createRequestAccount.toServiceRequest().toEntity());
        return AccountServiceResponse.of(account);
    }

}
