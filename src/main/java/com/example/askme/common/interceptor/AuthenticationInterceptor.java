package com.example.askme.common.interceptor;

import com.example.askme.common.error.ErrorCode;
import com.example.askme.common.error.exception.BusinessException;
import com.example.askme.common.jwt.TokenManager;
import com.example.askme.common.jwt.constatnt.TokenType;
import com.example.askme.common.util.AuthorizationHeaderUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorizationHeader = request.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorizationHeader(authorizationHeader);

        String token = authorizationHeader.split(" ")[1];
        tokenManager.validateToken(token);

        Claims tokenClaims = tokenManager.getTokenClaims(token);
        String tokenType = tokenClaims.getSubject();
        if(!TokenType.isAccessToken(tokenType)) {
            throw new BusinessException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        return true;
    }
}
