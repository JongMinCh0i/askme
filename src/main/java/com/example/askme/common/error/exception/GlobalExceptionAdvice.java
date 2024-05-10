package com.example.askme.common.error.exception;

import com.example.askme.common.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    protected ResultResponse<Void> handleInternalServerError(Exception e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[{}] Internal Server Error. message={}", errorId, e.getMessage(), e);
        return ResultResponse.fail(
                String.format(
                        "[%s] 서버 내부 오류가 발생했습니다", errorId)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    protected ResultResponse<Void> handleBusinessException(BusinessException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("[{}] Business Exception. message={}", errorId, e.getMessage(), e);
        return ResultResponse.fail(
                String.format(
                        "[%s] 에러 코드: %s 알 수 없는 오류가 발생했습니다. 메시지: %s", errorId, e.getErrorCode(), e.getMessage())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResultResponse<String> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorId = UUID.randomUUID().toString();
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        log.error("[{}] Validation error: {}", errorId, errors);

        String errorMessage = String.format(
                "[%s] 입력 값을 다시 확인해 주세요. 문제가 발생한 필드: %s",
                errorId, String.join(", ", errors));

        return ResultResponse.fail(errorMessage);
    }
}
