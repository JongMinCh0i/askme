package com.example.askme.api.controller.login;

import com.example.askme.api.controller.login.request.OauthLoginRequest;
import com.example.askme.api.controller.login.response.OauthLoginResponse;
import com.example.askme.api.controller.login.validator.OauthValidator;
import com.example.askme.api.service.oauth.OauthLoginService;
import com.example.askme.common.ResultResponse;
import com.example.askme.common.constant.LoginType;
import com.example.askme.common.resolver.memberInfo.TokenDto;
import com.example.askme.common.resolver.memberInfo.TokenParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OauthValidator oauthValidator;
    private final OauthLoginService oauthService;

    @PostMapping("/login")
    public ResultResponse<OauthLoginResponse> login(@RequestBody OauthLoginRequest oauthLoginRequest, @TokenParser TokenDto tokenDto) {
        oauthValidator.validateMemberType(oauthLoginRequest.getLoginType());
        OauthLoginResponse jwtTokenResponse = oauthService.login(tokenDto.getToken(), LoginType.valueOf(oauthLoginRequest.getLoginType()));
        return ResultResponse.success(jwtTokenResponse);
    }
}
