package com.portfolio.commerce.service;

import com.portfolio.commerce.dto.ProductRequest;
import com.portfolio.commerce.entity.Product;
import com.portfolio.commerce.mapper.ProductMapper;
import com.portfolio.commerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldCreateProduct() {
        var request = new ProductRequest("Notebook", "Business laptop", new BigDecimal("7999.90"), 10);
        var product = new Product(request.name(), request.description(), request.price(), request.stockQuantity());

        when(mapper.toEntity(request)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponse(product)).thenCallRealMethod();

        var response = service.create(request);

        assertThat(response.name()).isEqualTo("Notebook");
        assertThat(response.price()).isEqualByComparingTo("7999.90");
        verify(repository).save(any(Product.class));
    }
}
