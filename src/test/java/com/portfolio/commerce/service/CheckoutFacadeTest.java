package com.portfolio.commerce.service;

import com.portfolio.commerce.dto.CreateOrderRequest;
import com.portfolio.commerce.dto.OrderItemRequest;
import com.portfolio.commerce.entity.Customer;
import com.portfolio.commerce.entity.PaymentMethod;
import com.portfolio.commerce.entity.Product;
import com.portfolio.commerce.entity.ShippingMethod;
import com.portfolio.commerce.facade.CheckoutFacade;
import com.portfolio.commerce.mapper.OrderMapper;
import com.portfolio.commerce.repository.CustomerRepository;
import com.portfolio.commerce.repository.OrderRepository;
import com.portfolio.commerce.repository.ProductRepository;
import com.portfolio.commerce.strategy.PaymentStrategyFactory;
import com.portfolio.commerce.strategy.PixPaymentStrategy;
import com.portfolio.commerce.strategy.ShippingStrategyFactory;
import com.portfolio.commerce.strategy.StandardShippingStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CheckoutFacadeTest {

    @Test
    void shouldCreatePaidOrderAndDebitStock() {
        var customerRepository = mock(CustomerRepository.class);
        var productRepository = mock(ProductRepository.class);
        var orderRepository = mock(OrderRepository.class);
        var customer = new Customer("Ada Lovelace", "ada@commerce.dev", "12345678901");
        var product = new Product("Keyboard", "Mechanical keyboard", new BigDecimal("200.00"), 5);
        var productId = UUID.randomUUID();
        ReflectionTestUtils.setField(product, "id", productId);

        var request = new CreateOrderRequest(
                UUID.randomUUID(),
                PaymentMethod.PIX,
                ShippingMethod.STANDARD,
                List.of(new OrderItemRequest(productId, 2))
        );

        when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(customer));
        when(productRepository.findByIdIn(List.of(request.items().get(0).productId()))).thenReturn(List.of(product));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var facade = new CheckoutFacade(
                customerRepository,
                productRepository,
                orderRepository,
                new PaymentStrategyFactory(List.of(new PixPaymentStrategy())),
                new ShippingStrategyFactory(List.of(new StandardShippingStrategy())),
                new OrderMapper()
        );

        var response = facade.checkout(request);

        assertThat(response.status().name()).isEqualTo("PAID");
        assertThat(response.itemsTotal()).isEqualByComparingTo("400.00");
        assertThat(response.shippingTotal()).isEqualByComparingTo("0.00");
        assertThat(product.getStockQuantity()).isEqualTo(3);
    }
}
