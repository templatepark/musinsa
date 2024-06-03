package com.musinsa.product.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.musinsa.product.domain.Money;
import com.musinsa.product.domain.Product;

public record ProductSaveRequest(
        @NotNull @Min(1L) Long brandId,
        @NotNull @Min(1L) Long categoryId,
        @NotNull @Min(1L) BigDecimal price) {

    public Product toProduct() {
        return new Product(categoryId, brandId, new Money(price));
    }
}
