package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.Order;
import com.portfolio.commerce.entity.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod method() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public PaymentResult authorize(Order order) {
        log.info("Authorizing credit card payment for orderId={} amount={}", order.getId(), order.getGrandTotal());
        return PaymentResult.approved("cc-" + order.getId());
    }
}
