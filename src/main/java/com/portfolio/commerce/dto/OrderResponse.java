package com.portfolio.commerce.dto;

import com.portfolio.commerce.entity.OrderStatus;
import com.portfolio.commerce.entity.PaymentMethod;
import com.portfolio.commerce.entity.ShippingMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        String customerName,
        OrderStatus status,
        PaymentMethod paymentMethod,
        ShippingMethod shippingMethod,
        BigDecimal itemsTotal,
        BigDecimal shippingTotal,
        BigDecimal grandTotal,
        Instant createdAt,
        List<OrderItemResponse> items
) {
}
