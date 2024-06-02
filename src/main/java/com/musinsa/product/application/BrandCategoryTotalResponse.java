package com.musinsa.product.application;

import java.math.BigDecimal;
import java.util.List;

public record BrandCategoryTotalResponse(
        String brand, List<CategoryPriceResponse> categories, BigDecimal totalPrice) {}
