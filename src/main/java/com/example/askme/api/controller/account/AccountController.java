package com.example.askme.api.controller.account;

import com.example.askme.api.controller.account.request.AccountCreateRequest;
import com.example.askme.api.service.account.AccountInfoService;
import com.example.askme.api.service.account.AccountService;
import com.example.askme.api.service.account.response.AccountServiceResponse;
import com.example.askme.common.ResultResponse;
import com.example.askme.common.jwt.TokenManager;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final TokenManager tokenManager;
    private final AccountInfoService accountInfoService;
    private final AccountService accountService;

    @PostMapping("/signup")
    public ResultResponse<AccountServiceResponse> signUp(@Valid @RequestBody AccountCreateRequest createRequestAccount) {
        return ResultResponse.success(accountService.signUp(createRequestAccount.toServiceRequest()));
    }

    @GetMapping("/info")
    public ResultResponse<AccountServiceResponse> getAccountInfo(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String accessToken = authorizationHeader.split(" ")[1];
        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        return ResultResponse.success(accountInfoService.getAccountInfo(memberId));
    }
}
