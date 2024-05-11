package com.example.askme.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //계정
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "해당 계정이 존재하지 않습니다."),

    //게시글
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "002", "해당 게시글이 존재하지 않습니다."),

    //인증
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "A001", "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A002", "토큰이 만료되었습니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
