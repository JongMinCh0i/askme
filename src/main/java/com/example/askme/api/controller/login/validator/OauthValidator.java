package com.example.askme.api.controller.login.validator;

import com.example.askme.common.error.ErrorCode;
import com.example.askme.common.error.exception.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OauthValidator {

    public void validateMemberType(String loginType) {
        if (!StringUtils.hasText(loginType)) {
            throw new AuthenticationException(ErrorCode.NOT_EXIST_LOGIN_TYPE);
        }
    }

}
