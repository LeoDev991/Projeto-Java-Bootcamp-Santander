package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.Order;
import com.portfolio.commerce.entity.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class WalletPaymentStrategy implements PaymentStrategy {

    private static final BigDecimal SINGLE_TRANSACTION_LIMIT = new BigDecimal("5000.00");

    @Override
    public PaymentMethod method() {
        return PaymentMethod.WALLET;
    }

    @Override
    public PaymentResult authorize(Order order) {
        log.info("Authorizing wallet payment for orderId={} amount={}", order.getId(), order.getGrandTotal());
        if (order.getGrandTotal().compareTo(SINGLE_TRANSACTION_LIMIT) > 0) {
            return PaymentResult.refused("Wallet payment exceeds single transaction limit");
        }
        return PaymentResult.approved("wallet-" + order.getId());
    }
}
