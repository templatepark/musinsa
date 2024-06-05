package com.musinsa.product.domain;

import java.util.List;
import java.util.Optional;

import com.musinsa.product.application.dto.*;

public interface ProductQueryRepository {
    List<CategoryBrandPriceWithRank> getCategoryLowestPrices();

    Optional<BrandIdAndName> getLowestTotalBrandIdAndName();

    List<CategoryPrice> getBrandLowestTotalPrice(Long brandId);

    List<BrandNameAndPriceWithRank> getCategoryLowestAndHighestPrices(String categoryName);

    List<ProductWithDetails> findAllProducts();
}
