package com.example.askme.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //계정(MEMBER)
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "해당 계정이 존재하지 않습니다."),
    ALREADY_EXIST_USER_ID(HttpStatus.BAD_REQUEST, "M002", "이미 가입된 회원 입니다."),

    //게시글(POST)
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 게시글이 존재하지 않습니다."),

    //인증(AUTHENTICATION)
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "A001", "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A002", "토큰이 만료되었습니다."),
    NOT_EXIST_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "A003", "Authorization Header 존재하지 않습니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A004", "Bearer 타입이 아닙니다."),
    NOT_EXIST_LOGIN_TYPE(HttpStatus.UNAUTHORIZED, "A005", "LoginType 이 존재하지 않습니다."),
    NOT_EXIST_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A006", "해당 refresh token 은 존재하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A007", "해당 refresh token 은 만료 되었습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A008", "Access Token 타입이 아닙니다."),
    QUESTIONER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "A009", "질문자가 아닙니다."),

    //AWS(S3)
    BAD_REQUEST_IMAGE(HttpStatus.BAD_REQUEST, "S001", "이미지를 다시 확인해주세요."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "S002", "확장자를 다시 확인해주세요."),
    INVALID_AWS_CONNECTION(HttpStatus.BAD_REQUEST, "S003", "AWS 연결을 다시 확인해주세요"),
    UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "S004", "업로드에 실패했습니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
