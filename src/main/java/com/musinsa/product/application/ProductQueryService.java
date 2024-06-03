package com.musinsa.product.application;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.product.application.dto.BrandLowestPriceResponse;
import com.musinsa.product.application.dto.CategoryBrandPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestAndHighestPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestPriceResponse;
import com.musinsa.product.domain.ProductQueryRepository;

@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public ProductQueryService(ProductQueryRepository productQueryRepository) {
        this.productQueryRepository = productQueryRepository;
    }

    public CategoryLowestPriceResponse getCategoryLowestPrices() {
        List<CategoryBrandPriceResponse> categoryMinPrices =
                productQueryRepository.getCategoryLowestPrices();
        return new CategoryLowestPriceResponse(categoryMinPrices, getTotalPrice(categoryMinPrices));
    }

    private BigDecimal getTotalPrice(List<CategoryBrandPriceResponse> categoryMinPrices) {
        return categoryMinPrices.stream()
                .map(CategoryBrandPriceResponse::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BrandLowestPriceResponse getLowestTotalBrandPrice() {
        return productQueryRepository.getLowestTotalBrandPrice();
    }

    public CategoryLowestAndHighestPriceResponse getLowestAndHighestPricesByCategoryName(
            String categoryName) {
        return productQueryRepository.getLowestAndHighestPricesByCategoryName(categoryName);
    }
}
