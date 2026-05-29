package com.portfolio.commerce.mapper;

import com.portfolio.commerce.dto.OrderItemResponse;
import com.portfolio.commerce.dto.OrderResponse;
import com.portfolio.commerce.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getShippingMethod(),
                order.getItemsTotal(),
                order.getShippingTotal(),
                order.getGrandTotal(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getTotal()
                        ))
                        .toList()
        );
    }
}
