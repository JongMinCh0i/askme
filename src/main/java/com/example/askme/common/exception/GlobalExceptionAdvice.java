package com.example.askme.common.exception;

import com.example.askme.common.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    protected ResultResponse<Void> handleException(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        FieldError firstFieldError = fieldErrors.get(0);
        String errorMessage = String.format("[%s] %s", firstFieldError.getField(), firstFieldError.getDefaultMessage());

        String errorId = UUID.randomUUID().toString();

        log.error("[{}] Unknown Exception. message={}", errorId, e.getMessage(), e);
        return ResultResponse.fail(
                String.format(
                        "[%s] 알 수 없는 오류가 발생했습니다. 메시지: %s",
                        errorId,
                        errorMessage)
        );
    }
}
