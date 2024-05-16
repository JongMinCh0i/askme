package com.example.askme.api.controller.login;

import com.example.askme.api.controller.login.request.OauthLoginRequest;
import com.example.askme.api.controller.login.response.OauthLoginResponse;
import com.example.askme.api.controller.login.validator.OauthValidator;
import com.example.askme.api.service.oauth.OauthLoginService;
import com.example.askme.common.ResultResponse;
import com.example.askme.common.constant.LoginType;
import com.example.askme.common.util.AuthorizationHeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResultResponse<OauthLoginResponse> login(
            @RequestBody OauthLoginRequest oauthLoginRequest,
            HttpServletRequest httpServletRequest) {

        log.info(oauthLoginRequest.getLoginType());

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorizationHeader(authorizationHeader);
        oauthValidator.validateMemberType(oauthLoginRequest.getLoginType());

        String accessToken = authorizationHeader.split(" ")[1];

        OauthLoginResponse jwtTokenResponse = oauthService
                .login(accessToken, LoginType.valueOf(oauthLoginRequest.getLoginType()));

        return ResultResponse.success(jwtTokenResponse);
    }
}
