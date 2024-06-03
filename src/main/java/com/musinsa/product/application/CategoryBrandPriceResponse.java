package com.musinsa.product.application;

import java.math.BigDecimal;

public record CategoryBrandPriceResponse(String categoryName, String brandName, BigDecimal price) {}
