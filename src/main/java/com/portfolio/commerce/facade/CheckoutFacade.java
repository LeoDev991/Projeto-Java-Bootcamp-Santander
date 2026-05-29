package com.portfolio.commerce.facade;

import com.portfolio.commerce.dto.CreateOrderRequest;
import com.portfolio.commerce.dto.OrderResponse;
import com.portfolio.commerce.entity.Order;
import com.portfolio.commerce.exception.BusinessException;
import com.portfolio.commerce.exception.ResourceNotFoundException;
import com.portfolio.commerce.mapper.OrderMapper;
import com.portfolio.commerce.repository.CustomerRepository;
import com.portfolio.commerce.repository.OrderRepository;
import com.portfolio.commerce.repository.ProductRepository;
import com.portfolio.commerce.singleton.CheckoutConfigurationRegistry;
import com.portfolio.commerce.strategy.PaymentStrategyFactory;
import com.portfolio.commerce.strategy.ShippingStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckoutFacade {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final ShippingStrategyFactory shippingStrategyFactory;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse checkout(CreateOrderRequest request) {
        var customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.customerId()));
        if (!customer.isActive()) {
            throw new BusinessException("Inactive customers cannot place orders");
        }

        var productsById = productRepository.findByIdIn(
                        request.items().stream().map(item -> item.productId()).toList()
                ).stream()
                .collect(Collectors.toMap(product -> product.getId(), Function.identity()));

        var order = new Order(customer, request.paymentMethod(), request.shippingMethod());
        request.items().forEach(item -> {
            var product = productsById.get(item.productId());
            if (product == null || !product.isActive()) {
                throw new ResourceNotFoundException("Active product not found: " + item.productId());
            }
            product.debitStock(item.quantity());
            order.addItem(product, item.quantity(), product.getPrice());
        });

        var itemsTotal = order.getItems().stream()
                .map(item -> item.getTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var shipping = shippingStrategyFactory.resolve(request.shippingMethod()).calculate(itemsTotal);
        order.price(itemsTotal, shipping);
        validateFraudThreshold(order);

        var saved = orderRepository.save(order);
        var paymentResult = paymentStrategyFactory.resolve(request.paymentMethod()).authorize(saved);
        if (paymentResult.approved()) {
            saved.markPaid();
            log.info("Checkout approved orderId={} paymentReference={}", saved.getId(), paymentResult.providerReference());
        } else {
            saved.markPaymentFailed();
            log.warn("Checkout refused orderId={} reason={}", saved.getId(), paymentResult.reason());
        }
        return orderMapper.toResponse(saved);
    }

    private void validateFraudThreshold(Order order) {
        var threshold = CheckoutConfigurationRegistry.getInstance().getDecimal("fraud.max-order-without-review");
        if (order.getGrandTotal().compareTo(threshold) > 0) {
            throw new BusinessException("Order requires manual fraud review");
        }
    }
}
