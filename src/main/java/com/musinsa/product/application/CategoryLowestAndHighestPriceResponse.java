package com.musinsa.product.application;

import java.util.List;

public record CategoryLowestAndHighestPriceResponse(
        String categoryName,
        List<BrandPriceResponse> lowestBrandPrices,
        List<BrandPriceResponse> highestBrandPrices) {}
