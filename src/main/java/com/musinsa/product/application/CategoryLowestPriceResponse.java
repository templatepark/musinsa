package com.musinsa.product.application;

import java.math.BigDecimal;
import java.util.List;

public record CategoryLowestPriceResponse(
        List<CategoryBrandPriceResponse> categoryPrices, BigDecimal totalPrice) {}
