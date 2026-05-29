package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.ShippingMethod;

import java.math.BigDecimal;

public interface ShippingStrategy {
    ShippingMethod method();

    BigDecimal calculate(BigDecimal itemsTotal);
}
