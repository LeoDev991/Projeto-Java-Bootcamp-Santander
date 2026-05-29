package com.portfolio.commerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductPatchRequest(
        @Size(max = 100) String name,
        @Size(max = 500) String description,
        @DecimalMin("0.01") BigDecimal price,
        @Min(0) Integer stockQuantity
) {
}
