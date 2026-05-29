package com.portfolio.commerce.repository;

import com.portfolio.commerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByEmail(String email);

    boolean existsByDocument(String document);

    Optional<Customer> findByEmail(String email);
}
