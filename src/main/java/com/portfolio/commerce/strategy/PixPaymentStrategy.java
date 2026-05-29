package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.Order;
import com.portfolio.commerce.entity.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PixPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod method() {
        return PaymentMethod.PIX;
    }

    @Override
    public PaymentResult authorize(Order order) {
        log.info("Generating PIX charge for orderId={} amount={}", order.getId(), order.getGrandTotal());
        return PaymentResult.approved("pix-" + order.getId());
    }
}
