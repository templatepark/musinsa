package com.musinsa.product.domain;

import java.util.List;

import com.musinsa.product.application.CategoryBrandPriceResponse;

public interface ProductQueryRepository {
    List<CategoryBrandPriceResponse> getCategoryLowestPrices();
}
