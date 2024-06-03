package com.musinsa.product.domain;

import java.util.List;

import com.musinsa.product.application.dto.BrandLowestPriceResponse;
import com.musinsa.product.application.dto.CategoryBrandPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestAndHighestPriceResponse;

public interface ProductQueryRepository {
    List<CategoryBrandPriceResponse> getCategoryLowestPrices();

    BrandLowestPriceResponse getLowestTotalBrandPrice();

    CategoryLowestAndHighestPriceResponse getLowestAndHighestPricesByCategoryName(
            String categoryName);
}
