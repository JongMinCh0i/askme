package com.example.askme.api.controller.account;

import com.example.askme.api.controller.account.request.AccountCreateRequest;
import com.example.askme.api.service.account.AccountService;
import com.example.askme.api.service.account.response.AccountServiceResponse;
import com.example.askme.common.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResultResponse<AccountServiceResponse> signUp(@Valid @RequestBody AccountCreateRequest createRequestAccount) {
        return ResultResponse.success(accountService.signUp(createRequestAccount));
    }

}
