package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.ShippingMethod;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExpressShippingStrategy implements ShippingStrategy {

    @Override
    public ShippingMethod method() {
        return ShippingMethod.EXPRESS;
    }

    @Override
    public BigDecimal calculate(BigDecimal itemsTotal) {
        return itemsTotal.multiply(new BigDecimal("0.08")).max(new BigDecimal("39.90"));
    }
}
