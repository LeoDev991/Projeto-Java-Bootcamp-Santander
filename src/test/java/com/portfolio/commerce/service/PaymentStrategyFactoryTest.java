package com.portfolio.commerce.service;

import com.portfolio.commerce.entity.PaymentMethod;
import com.portfolio.commerce.exception.BusinessException;
import com.portfolio.commerce.strategy.CreditCardPaymentStrategy;
import com.portfolio.commerce.strategy.PaymentStrategyFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentStrategyFactoryTest {

    @Test
    void shouldResolveStrategyByPaymentMethod() {
        var factory = new PaymentStrategyFactory(List.of(new CreditCardPaymentStrategy()));

        assertThat(factory.resolve(PaymentMethod.CREDIT_CARD)).isInstanceOf(CreditCardPaymentStrategy.class);
    }

    @Test
    void shouldFailWhenStrategyDoesNotExist() {
        var factory = new PaymentStrategyFactory(List.of(new CreditCardPaymentStrategy()));

        assertThatThrownBy(() -> factory.resolve(PaymentMethod.PIX))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Unsupported payment method");
    }
}
