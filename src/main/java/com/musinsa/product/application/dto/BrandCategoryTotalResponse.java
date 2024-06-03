package com.musinsa.product.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record BrandCategoryTotalResponse(
        String brandName, List<CategoryPriceResponse> categories, BigDecimal totalPrice) {}
