package com.portfolio.commerce.strategy;

import com.portfolio.commerce.entity.ShippingMethod;
import com.portfolio.commerce.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ShippingStrategyFactory {

    private final Map<ShippingMethod, ShippingStrategy> strategies = new EnumMap<>(ShippingMethod.class);

    public ShippingStrategyFactory(List<ShippingStrategy> shippingStrategies) {
        shippingStrategies.forEach(strategy -> strategies.put(strategy.method(), strategy));
    }

    public ShippingStrategy resolve(ShippingMethod method) {
        ShippingStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new BusinessException("Unsupported shipping method: " + method);
        }
        return strategy;
    }
}
