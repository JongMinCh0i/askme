package com.example.askme.common;

public record ResultResponse<T>(
        boolean success,
        String message,
        T data
) {

    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(true, null, data);
    }

    public static <T> ResultResponse<T> fail(String message) {
        return new ResultResponse<>(false, message, null);
    }
}
