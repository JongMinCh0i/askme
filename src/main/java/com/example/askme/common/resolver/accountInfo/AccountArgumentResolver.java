package com.example.askme.common.resolver.accountInfo;

import com.example.askme.common.constant.Role;
import com.example.askme.common.jwt.TokenManager;
import com.example.askme.common.resolver.tokenInfo.TokenDto;
import com.example.askme.common.util.AuthorizationHeaderUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AccountArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenManager tokenManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(AccountInfo.class);
        boolean hasAccountInfoDto = AccountDto.class.isAssignableFrom(parameter.getParameterType());
        return hasParameterAnnotation && hasAccountInfoDto;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeader = nativeRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorizationHeader(authorizationHeader);
        String token = authorizationHeader.split(" ")[1];

        Claims tokenClaims = tokenManager.getTokenClaims(token);
        Long accountId = tokenClaims.get("memberId", Long.class);
        String role = (String) tokenClaims.get("role");

        return AccountDto.builder()
                .accountId(accountId)
                .role(Role.valueOf(role))
                .build();
    }
}
