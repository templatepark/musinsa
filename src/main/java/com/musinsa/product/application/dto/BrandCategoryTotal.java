package com.musinsa.product.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record BrandCategoryTotal(
        String brandName, List<CategoryPrice> categories, BigDecimal totalPrice) {}
