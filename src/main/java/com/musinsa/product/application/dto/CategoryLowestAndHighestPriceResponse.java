package com.musinsa.product.application.dto;

import java.util.List;

public record CategoryLowestAndHighestPriceResponse(
        String categoryName,
        List<BrandPrice> lowestBrandPrices,
        List<BrandPrice> highestBrandPrices) {}
