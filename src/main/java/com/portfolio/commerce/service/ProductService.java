package com.portfolio.commerce.service;

import com.portfolio.commerce.dto.ProductPatchRequest;
import com.portfolio.commerce.dto.ProductRequest;
import com.portfolio.commerce.dto.ProductResponse;
import com.portfolio.commerce.exception.ResourceNotFoundException;
import com.portfolio.commerce.mapper.ProductMapper;
import com.portfolio.commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Transactional
    public ProductResponse create(ProductRequest request) {
        var product = repository.save(mapper.toEntity(request));
        log.info("Product created id={} price={}", product.getId(), product.getPrice());
        return mapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(String name, Boolean active, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return repository.search(name, active, minPrice, maxPrice, pageable).map(mapper::toResponse);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        var product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        product.update(request.name(), request.description(), request.price(), request.stockQuantity());
        log.info("Product updated id={}", id);
        return mapper.toResponse(product);
    }

    @Transactional
    public ProductResponse patch(UUID id, ProductPatchRequest request) {
        var product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        var merged = mapper.merge(product, request);
        product.update(merged.name(), merged.description(), merged.price(), merged.stockQuantity());
        log.info("Product partially updated id={}", id);
        return mapper.toResponse(product);
    }

    @Transactional
    public void delete(UUID id) {
        var product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        product.deactivate();
        log.info("Product deactivated id={}", id);
    }
}
