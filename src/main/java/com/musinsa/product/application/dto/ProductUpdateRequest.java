package com.musinsa.product.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;

import com.musinsa.product.domain.Money;
import com.musinsa.product.domain.Product;

public record ProductUpdateRequest(
        @Min(1L) Long brandId, @Min(1L) Long categoryId, @Min(1L) BigDecimal price) {
    public Product toProduct() {
        return new Product(categoryId, brandId, new Money(price));
    }
}
