package com.example.askme.common.jwt.constatnt;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS, REFRESH;

    public static boolean isAccessToken(String subject) {
        return ACCESS.name().equals(subject);
    }
}
