package com.musinsa.product.domain;

import java.util.List;

import com.musinsa.product.application.BrandLowestPriceResponse;
import com.musinsa.product.application.CategoryBrandPriceResponse;
import com.musinsa.product.application.CategoryLowestAndHighestPriceResponse;

public interface ProductQueryRepository {
    List<CategoryBrandPriceResponse> getCategoryLowestPrices();

    BrandLowestPriceResponse getLowestTotalBrandPrice();

    CategoryLowestAndHighestPriceResponse getLowestAndHighestPricesByCategoryName(
            String categoryName);
}
