package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.ShippingMethod;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StandardShippingStrategy implements ShippingStrategy {

    @Override
    public ShippingMethod method() {
        return ShippingMethod.STANDARD;
    }

    @Override
    public BigDecimal calculate(BigDecimal itemsTotal) {
        if (itemsTotal.compareTo(new BigDecimal("300.00")) >= 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal("19.90");
    }
}
