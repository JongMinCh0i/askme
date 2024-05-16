package com.example.askme.common.util;

import com.example.askme.common.error.ErrorCode;
import com.example.askme.common.error.exception.AuthenticationException;
import org.springframework.util.StringUtils;

public class AuthorizationHeaderUtils {

    public static void validateAuthorizationHeader(String authorizationHeader) {

        // 1. Authorization Header 가 존재하지 않을 경우
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new AuthenticationException(ErrorCode.NOT_EXIST_AUTHORIZATION_HEADER);
        }

        // 2. Bearer 타입이 아닐 경우
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
        }
    }
}
