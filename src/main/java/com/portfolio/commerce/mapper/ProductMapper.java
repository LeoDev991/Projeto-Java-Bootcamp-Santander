package com.portfolio.commerce.mapper;

import com.portfolio.commerce.dto.ProductPatchRequest;
import com.portfolio.commerce.dto.ProductRequest;
import com.portfolio.commerce.dto.ProductResponse;
import com.portfolio.commerce.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        return new Product(request.name(), request.description(), request.price(), request.stockQuantity());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.isActive()
        );
    }

    public ProductRequest merge(Product product, ProductPatchRequest request) {
        return new ProductRequest(
                request.name() == null ? product.getName() : request.name(),
                request.description() == null ? product.getDescription() : request.description(),
                request.price() == null ? product.getPrice() : request.price(),
                request.stockQuantity() == null ? product.getStockQuantity() : request.stockQuantity()
        );
    }
}
