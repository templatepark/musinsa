package com.musinsa.product.application.dto;

import java.math.BigDecimal;

public record CategoryBrandPriceResponse(String categoryName, String brandName, BigDecimal price) {}
