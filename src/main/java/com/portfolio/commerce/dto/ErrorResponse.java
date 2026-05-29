package com.portfolio.commerce.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String path,
        List<FieldErrorResponse> fields
) {
}
