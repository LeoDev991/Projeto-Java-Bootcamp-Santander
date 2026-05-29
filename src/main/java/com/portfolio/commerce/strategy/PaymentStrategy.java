package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.Order;
import com.portfolio.commerce.entity.PaymentMethod;

public interface PaymentStrategy {
    PaymentMethod method();

    PaymentResult authorize(Order order);
}
