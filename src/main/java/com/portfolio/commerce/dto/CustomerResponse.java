package com.portfolio.commerce.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String email,
        String document,
        boolean active,
        Instant createdAt
) {
}
