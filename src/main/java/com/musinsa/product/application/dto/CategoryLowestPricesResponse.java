package com.musinsa.product.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record CategoryLowestPricesResponse(
        List<CategoryBrandPrice> categoryPrices, BigDecimal totalPrice) {}
