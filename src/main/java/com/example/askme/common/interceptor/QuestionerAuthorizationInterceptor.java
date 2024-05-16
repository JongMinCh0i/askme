package com.example.askme.common.interceptor;

import com.example.askme.common.constant.Role;
import com.example.askme.common.error.ErrorCode;
import com.example.askme.common.error.exception.AuthenticationException;
import com.example.askme.common.jwt.TokenManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class QuestionerAuthorizationInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization)");
        String accessToken = authorizationHeader.split(" ")[1];

        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String role = (String) tokenClaims.get("role");

        if(!Role.QUESTIONER.equals(Role.valueOf(role))) {
            throw new AuthenticationException(ErrorCode.QUESTIONER_NOT_AUTHORIZED);
        }

        return true;
    }
}
