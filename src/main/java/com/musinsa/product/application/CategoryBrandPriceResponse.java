package com.musinsa.product.application;

import java.math.BigDecimal;

public record CategoryBrandPriceResponse(String category, String brand, BigDecimal price) {}
