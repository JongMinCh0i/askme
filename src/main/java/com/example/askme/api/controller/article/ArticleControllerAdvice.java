package com.example.askme.api.controller.article;

import com.example.askme.common.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class ArticleControllerAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    protected ResultResponse<Void> handleException(Exception e) {
        String errorId = UUID.randomUUID().toString();

        log.error("[{}] Unknown Exception. message={}", errorId, e.getMessage(), e);
        return ResultResponse.fail(
                String.format(
                        "알 수 없는 오류가 발생했습니다. 메시지: %s",
                        errorId,
                        e.getMessage())
        );
    }
}
