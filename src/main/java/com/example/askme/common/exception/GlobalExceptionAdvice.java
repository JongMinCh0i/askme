package com.example.askme.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public String handlePageNotFoundException(BusinessException e) {
        log.info("PageNotFoundException: {}", e.getMessage());
        return e.getMessage();
    }
}
