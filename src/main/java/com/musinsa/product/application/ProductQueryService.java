package com.musinsa.product.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.common.constants.CacheNames;
import com.musinsa.product.application.dto.*;
import com.musinsa.product.domain.ProductQueryRepository;

@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public ProductQueryService(ProductQueryRepository productQueryRepository) {
        this.productQueryRepository = productQueryRepository;
    }

    @Cacheable(CacheNames.CATEGORY_LOWEST_PRICES)
    public CategoryLowestPricesResponse getCategoryLowestPrices() {
        List<CategoryBrandPrice> categoryBrandPrices =
                productQueryRepository.getCategoryLowestPrices().stream()
                        .filter(categoryBrandPriceRow -> categoryBrandPriceRow.rank() == 1L)
                        .map(
                                categoryBrandPriceRow ->
                                        new CategoryBrandPrice(
                                                categoryBrandPriceRow.categoryName(),
                                                categoryBrandPriceRow.brandName(),
                                                categoryBrandPriceRow.price()))
                        .toList();
        return new CategoryLowestPricesResponse(
                categoryBrandPrices,
                calculateTotalPrice(
                        categoryBrandPrices.stream().map(CategoryBrandPrice::price).toList()));
    }

    @Cacheable(CacheNames.BRAND_TOTAL_LOWEST_PRICE)
    public BrandLowestTotalPriceResponse getBrandLowestTotalPrice() {
        Optional<BrandIdAndName> optionalBrandIdAndName =
                productQueryRepository.getLowestTotalBrandIdAndName();
        if (optionalBrandIdAndName.isEmpty()) {
            return BrandLowestTotalPriceResponse.empty();
        }

        BrandIdAndName brandIdAndName = optionalBrandIdAndName.get();
        List<CategoryPrice> categoryPrices =
                productQueryRepository.getBrandLowestTotalPrice(brandIdAndName.brandId());

        return new BrandLowestTotalPriceResponse(
                new BrandCategoryTotal(
                        brandIdAndName.brandName(),
                        categoryPrices,
                        calculateTotalPrice(
                                categoryPrices.stream().map(CategoryPrice::price).toList())));
    }

    private BigDecimal calculateTotalPrice(List<BigDecimal> categoryPriceResponses) {
        return categoryPriceResponses.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Cacheable(CacheNames.CATEGORY_LOWEST_HIGHEST_PRICES)
    public CategoryLowestAndHighestPriceResponse getCategoryLowestAndHighestPrices(
            String categoryName) {
        List<BrandNameAndPriceWithRank> brandNameAnxPrices =
                productQueryRepository.getCategoryLowestAndHighestPrices(categoryName);

        return new CategoryLowestAndHighestPriceResponse(
                categoryName,
                getLowestBrandPrices(brandNameAnxPrices),
                getHighestBrandPrices(brandNameAnxPrices));
    }

    private List<BrandPrice> getLowestBrandPrices(List<BrandNameAndPriceWithRank> requests) {
        return getLowestOrHighestBrandPrices(requests, BrandNameAndPriceWithRank::minRank);
    }

    private List<BrandPrice> getHighestBrandPrices(List<BrandNameAndPriceWithRank> requests) {
        return getLowestOrHighestBrandPrices(requests, BrandNameAndPriceWithRank::maxRank);
    }

    private List<BrandPrice> getLowestOrHighestBrandPrices(
            List<BrandNameAndPriceWithRank> requests,
            Function<BrandNameAndPriceWithRank, Long> rankExtractor) {
        return requests.stream()
                .filter(request -> rankExtractor.apply(request) == 1L)
                .map(request -> new BrandPrice(request.brandName(), request.price()))
                .toList();
    }

    public List<ProductWithDetails> findAllProducts() {
        return productQueryRepository.findAllProducts();
    }
}
