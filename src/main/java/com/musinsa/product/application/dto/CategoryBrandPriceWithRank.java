package com.musinsa.product.application.dto;

import java.math.BigDecimal;

public record CategoryBrandPriceWithRank(
        String categoryName, String brandName, BigDecimal price, Long rank) {}
