package com.portfolio.commerce.controller;

import com.portfolio.commerce.dto.ApiResponse;
import com.portfolio.commerce.dto.CreateOrderRequest;
import com.portfolio.commerce.dto.OrderResponse;
import com.portfolio.commerce.dto.OrderStatusPatchRequest;
import com.portfolio.commerce.entity.OrderStatus;
import com.portfolio.commerce.facade.CheckoutFacade;
import com.portfolio.commerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final CheckoutFacade checkoutFacade;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created", checkoutFacade.checkout(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Order found", orderService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> search(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) OrderStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success("Orders listed", orderService.search(customerId, status, pageable)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> patchStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusPatchRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Order status updated", orderService.patchStatus(id, request.status())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
