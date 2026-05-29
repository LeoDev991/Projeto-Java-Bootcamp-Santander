package com.portfolio.commerce.dto;

import com.portfolio.commerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusPatchRequest(@NotNull OrderStatus status) {
}
