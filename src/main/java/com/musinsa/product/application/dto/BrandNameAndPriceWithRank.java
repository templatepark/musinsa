package com.musinsa.product.application.dto;

import java.math.BigDecimal;

public record BrandNameAndPriceWithRank(
        String brandName, BigDecimal price, Long minRank, Long maxRank) {}
