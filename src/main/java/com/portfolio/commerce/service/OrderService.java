package com.portfolio.commerce.service;

import com.portfolio.commerce.dto.OrderResponse;
import com.portfolio.commerce.entity.OrderStatus;
import com.portfolio.commerce.exception.BusinessException;
import com.portfolio.commerce.exception.ResourceNotFoundException;
import com.portfolio.commerce.mapper.OrderMapper;
import com.portfolio.commerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Transactional(readOnly = true)
    public OrderResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> search(UUID customerId, OrderStatus status, Pageable pageable) {
        return repository.search(customerId, status, pageable).map(mapper::toResponse);
    }

    @Transactional
    public OrderResponse patchStatus(UUID id, OrderStatus status) {
        var order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        if (status == OrderStatus.CANCELLED) {
            order.cancel();
            return mapper.toResponse(order);
        }
        throw new BusinessException("Only cancellation is supported by this endpoint");
    }

    @Transactional
    public void delete(UUID id) {
        patchStatus(id, OrderStatus.CANCELLED);
    }
}
