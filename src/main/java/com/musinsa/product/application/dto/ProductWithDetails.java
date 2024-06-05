package com.musinsa.product.application.dto;

import java.math.BigDecimal;

public record ProductWithDetails(
        Long id, BigDecimal price, String brandName, String categoryName) {}
