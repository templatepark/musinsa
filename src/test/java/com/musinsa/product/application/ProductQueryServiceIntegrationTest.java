package com.musinsa.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.brand.domain.Brand;
import com.musinsa.brand.domain.BrandRepository;
import com.musinsa.category.domain.Category;
import com.musinsa.category.domain.CategoryRepository;
import com.musinsa.product.application.dto.*;
import com.musinsa.product.domain.Money;
import com.musinsa.product.domain.Product;
import com.musinsa.product.domain.ProductRepository;

@Transactional
@SpringBootTest
public class ProductQueryServiceIntegrationTest {

    @Autowired private ProductQueryService service;

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private BrandRepository brandRepository;
    @Autowired private CacheManager cacheManager;
    private Brand brandA;
    private Brand brandB;
    private Brand brandC;
    private Brand brandD;
    private Category categoryA;
    private Category categoryB;

    @BeforeEach
    void setup() {
        brandA = brandRepository.save(new Brand("BRAND_A"));
        brandB = brandRepository.save(new Brand("BRAND_B"));
        brandC = brandRepository.save(new Brand("BRAND_C"));
        brandD = brandRepository.save(new Brand("BRAND_D"));
        categoryA = categoryRepository.save(new Category("CATEGORY_A"));
        categoryB = categoryRepository.save(new Category("CATEGORY_B"));
        clearCache();
    }

    private void clearCache() {
        cacheManager
                .getCacheNames()
                .forEach(
                        cacheName -> {
                            cacheManager.getCache(cacheName).clear();
                        });
    }

    private void createProducts(List<Product> products) {
        productRepository.saveAll(products);
    }

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 데이터가 존재할 경우")
    @Test
    void getCategoryLowestPrices_WhenDataExist_ShouldReturnData() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal highestPrice = new BigDecimal(1000L);
        BigDecimal expectedTotalPrice = lowestPrice.add(lowestPrice);

        createProducts(
                List.of(
                        new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)),
                        new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)),
                        new Product(brandA.getId(), categoryB.getId(), new Money(lowestPrice)),
                        new Product(brandB.getId(), categoryB.getId(), new Money(highestPrice))));

        // when
        CategoryLowestPricesResponse result = service.getCategoryLowestPrices();

        // then
        assertThat(result.categoryPrices())
                .filteredOn(cate -> cate.categoryName().equals(categoryA.getName()))
                .extracting(CategoryBrandPrice::brandName)
                .containsExactly(brandB.getName());

        assertThat(result.categoryPrices())
                .filteredOn(cate -> cate.categoryName().equals(categoryB.getName()))
                .extracting(CategoryBrandPrice::brandName)
                .containsExactly(brandA.getName());

        assertThat(result.totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getCategoryLowestPrices_WhenDataNotExist_ShouldReturnEmptyData() {
        // given
        BigDecimal expectedTotalPrice = new BigDecimal(0L);

        // when
        CategoryLowestPricesResponse result = service.getCategoryLowestPrices();

        // then
        assertThat(result.categoryPrices()).isEmpty();
        assertThat(result.totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 같은 최저가 브랜드 존재 시, 이름 오름차순으로 조회")
    @Test
    void getCategoryLowestPrices_WhenDupLowestPriceBrandExist_ShouldReturnDataByBrandNameASC() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);

        createProducts(
                List.of(
                        new Product(brandA.getId(), categoryA.getId(), new Money(lowestPrice)),
                        new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice))));

        // when
        CategoryLowestPricesResponse result = service.getCategoryLowestPrices();

        // then
        assertThat(result.categoryPrices())
                .filteredOn(cate -> cate.categoryName().equals(categoryA.getName()))
                .extracting(CategoryBrandPrice::brandName)
                .containsExactly(brandA.getName());
        assertThat(result.totalPrice()).isEqualByComparingTo(lowestPrice);
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 데이터가 존재할 경우")
    @Test
    void getLowestTotalBrandPrice_WhenDataExist_ShouldReturnData() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal highestPrice = new BigDecimal(1000L);
        BigDecimal expectedTotalPrice = lowestPrice.add(lowestPrice);

        createProducts(
                List.of(
                        new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)),
                        new Product(brandA.getId(), categoryB.getId(), new Money(highestPrice)),
                        new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)),
                        new Product(brandB.getId(), categoryB.getId(), new Money(lowestPrice))));

        // when
        BrandLowestTotalPriceResponse result = service.getBrandLowestTotalPrice();

        // then
        assertThat(result.lowestPrice().brandName()).isEqualTo(brandB.getName());
        assertThat(result.lowestPrice().categories())
                .extracting(CategoryPrice::categoryName)
                .containsExactly(categoryA.getName(), categoryB.getName());
        assertThat(result.lowestPrice().totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getLowestTotalBrandPrice_WhenDataNotExist_ShouldReturnEmpty() {
        // given
        BigDecimal expectedTotalPrice = new BigDecimal(0L);

        // when
        BrandLowestTotalPriceResponse result = service.getBrandLowestTotalPrice();

        // then
        assertThat(result.lowestPrice().brandName()).isNull();
        assertThat(result.lowestPrice().categories()).isEmpty();
        assertThat(result.lowestPrice().totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 같은 최저가 브랜드 존재 시, 이름 오름차순으로 조회")
    @Test
    void getLowestTotalBrandPrice_WhenDupLowestPriceBrandExist_ShouldReturnData() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal expectedTotalPrice = lowestPrice.add(lowestPrice);

        createProducts(
                List.of(
                        new Product(brandA.getId(), categoryA.getId(), new Money(lowestPrice)),
                        new Product(brandA.getId(), categoryB.getId(), new Money(lowestPrice)),
                        new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)),
                        new Product(brandB.getId(), categoryB.getId(), new Money(lowestPrice))));

        // when
        BrandLowestTotalPriceResponse result = service.getBrandLowestTotalPrice();

        // then
        assertThat(result.lowestPrice().brandName()).isEqualTo(brandA.getName());
        assertThat(result.lowestPrice().categories())
                .extracting(CategoryPrice::categoryName)
                .containsExactly(categoryA.getName(), categoryB.getName());
        assertThat(result.lowestPrice().totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회: 데이터가 존재할 경우")
    @Test
    void getLowestAndHighestPricesByCategoryName_WhenDataExist_ShouldReturnData() {
        // given
        String categoryName = "CATEGORY_A";
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal highestPrice = new BigDecimal(1000L);

        productRepository.saveAll(
                List.of(
                        new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)),
                        new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice))));

        // when
        CategoryLowestAndHighestPriceResponse result =
                service.getCategoryLowestAndHighestPrices(categoryName);

        // then
        assertThat(result.categoryName()).isEqualTo(categoryName);
        assertThat(result.highestBrandPrices())
                .filteredOn(cate -> cate.price().compareTo(highestPrice) == 0)
                .extracting(BrandPrice::brandName)
                .containsExactly(brandA.getName());
        assertThat(result.lowestBrandPrices())
                .filteredOn(cate -> cate.price().compareTo(lowestPrice) == 0)
                .extracting(BrandPrice::brandName)
                .containsExactly(brandB.getName());
    }

    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getLowestAndHighestPricesByCategoryName_WhenDataNotExist_ShouldReturnData() {
        // given
        String categoryName = "CATEGORY_A";

        // when
        CategoryLowestAndHighestPriceResponse result =
                service.getCategoryLowestAndHighestPrices(categoryName);

        // then
        assertThat(result.categoryName()).isEqualTo(categoryName);
        assertThat(result.lowestBrandPrices()).isEmpty();
        assertThat(result.highestBrandPrices()).isEmpty();
    }

    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회: 같은 최저가 또는 최고가 브랜드 존재 시 다건 조회")
    @Test
    void getLowestAndHighestPricesByCategoryName_WhenDupPriceBrandExist_ShouldReturnData() {
        // given
        String categoryName = "CATEGORY_A";
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal highestPrice = new BigDecimal(1000L);

        productRepository.saveAll(
                List.of(
                        new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)),
                        new Product(brandB.getId(), categoryA.getId(), new Money(highestPrice)),
                        new Product(brandC.getId(), categoryA.getId(), new Money(lowestPrice)),
                        new Product(brandD.getId(), categoryA.getId(), new Money(lowestPrice))));

        // when
        CategoryLowestAndHighestPriceResponse result =
                service.getCategoryLowestAndHighestPrices(categoryName);

        // then
        assertThat(result.categoryName()).isEqualTo(categoryName);
        assertThat(result.lowestBrandPrices().size()).isEqualTo(2);
        assertThat(result.highestBrandPrices().size()).isEqualTo(2);
    }
}
