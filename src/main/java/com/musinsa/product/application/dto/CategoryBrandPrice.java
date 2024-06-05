package com.musinsa.product.application.dto;

import java.math.BigDecimal;

public record CategoryBrandPrice(String categoryName, String brandName, BigDecimal price) {}
