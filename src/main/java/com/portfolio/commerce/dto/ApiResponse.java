package com.portfolio.commerce.dto;

import java.time.Instant;

public record ApiResponse<T>(
        Instant timestamp,
        boolean success,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(Instant.now(), true, message, data);
    }

    public static <T> ApiResponse<T> failure(String message, T data) {
        return new ApiResponse<>(Instant.now(), false, message, data);
    }
}
