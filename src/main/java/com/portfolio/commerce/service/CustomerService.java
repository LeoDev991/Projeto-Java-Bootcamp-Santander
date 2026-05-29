package com.portfolio.commerce.service;

import com.portfolio.commerce.dto.CustomerRequest;
import com.portfolio.commerce.dto.CustomerResponse;
import com.portfolio.commerce.exception.DuplicateResourceException;
import com.portfolio.commerce.exception.ResourceNotFoundException;
import com.portfolio.commerce.mapper.CustomerMapper;
import com.portfolio.commerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (repository.existsByEmail(request.email()) || repository.existsByDocument(request.document())) {
            throw new DuplicateResourceException("Customer already exists with same email or document");
        }
        var customer = repository.save(mapper.toEntity(request));
        log.info("Customer created id={}", customer.getId());
        return mapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    public CustomerResponse update(UUID id, CustomerRequest request) {
        var customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        customer.updateProfile(request.name(), request.email());
        log.info("Customer updated id={}", id);
        return mapper.toResponse(customer);
    }

    @Transactional
    public void delete(UUID id) {
        var customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        customer.deactivate();
        log.info("Customer deactivated id={}", id);
    }
}
