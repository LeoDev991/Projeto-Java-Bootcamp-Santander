package com.portfolio.commerce.repository;

import com.portfolio.commerce.entity.Order;
import com.portfolio.commerce.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    @Query("""
            select o from Order o
            where (:customerId is null or o.customer.id = :customerId)
              and (:status is null or o.status = :status)
            """)
    Page<Order> search(
            @Param("customerId") UUID customerId,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}
