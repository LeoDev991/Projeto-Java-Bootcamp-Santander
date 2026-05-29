package com.portfolio.commerce.dto;

import com.portfolio.commerce.entity.PaymentMethod;
import com.portfolio.commerce.entity.ShippingMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotNull PaymentMethod paymentMethod,
        @NotNull ShippingMethod shippingMethod,
        @Valid @NotEmpty List<OrderItemRequest> items
) {
}
