package com.musinsa.product.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record BrandLowestTotalPriceResponse(BrandCategoryTotal lowestPrice) {
    public static BrandLowestTotalPriceResponse empty() {
        return new BrandLowestTotalPriceResponse(
                new BrandCategoryTotal(null, List.of(), new BigDecimal(0L)));
    }
}
