package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.PaymentMethod;
import com.portfolio.commerce.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentStrategyFactory {

    private final Map<PaymentMethod, PaymentStrategy> strategies = new EnumMap<>(PaymentMethod.class);

    public PaymentStrategyFactory(List<PaymentStrategy> paymentStrategies) {
        paymentStrategies.forEach(strategy -> strategies.put(strategy.method(), strategy));
    }

    public PaymentStrategy resolve(PaymentMethod method) {
        PaymentStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new BusinessException("Unsupported payment method: " + method);
        }
        return strategy;
    }
}
