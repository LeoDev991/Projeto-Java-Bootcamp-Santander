package com.portfolio.commerce.mapper;

import com.portfolio.commerce.dto.CustomerRequest;
import com.portfolio.commerce.dto.CustomerResponse;
import com.portfolio.commerce.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        return new Customer(request.name(), request.email(), request.document());
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getDocument(),
                customer.isActive(),
                customer.getCreatedAt()
        );
    }
}
