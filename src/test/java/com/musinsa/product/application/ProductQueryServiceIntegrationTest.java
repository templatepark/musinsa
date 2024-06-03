package com.musinsa.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import com.musinsa.product.application.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.brand.domain.Brand;
import com.musinsa.brand.domain.BrandRepository;
import com.musinsa.category.domain.Category;
import com.musinsa.category.domain.CategoryRepository;
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

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 데이터가 존재할 경우")
    @Test
    void getCategoryLowestPrices_WhenDataExist_ShouldReturnData() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal highestPrice = new BigDecimal(1000L);
        BigDecimal expectedTotalPrice = lowestPrice.add(lowestPrice);

        Brand brandA = brandRepository.save(new Brand("BRAND_A"));
        Brand brandB = brandRepository.save(new Brand("BRAND_B"));
        Category categoryA = categoryRepository.save(new Category("CATEGORY_A"));
        Category categoryB = categoryRepository.save(new Category("CATEGORY_B"));

        productRepository.save(
                new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandA.getId(), categoryB.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryB.getId(), new Money(highestPrice)));
        // when
        CategoryLowestPriceResponse result = service.getCategoryLowestPrices();

        // then
        assertThat(result.categoryPrices())
                .filteredOn(cate -> cate.categoryName().equals("CATEGORY_A"))
                .extracting(CategoryBrandPriceResponse::brandName)
                .containsExactly(brandB.getName());

        assertThat(result.categoryPrices())
                .filteredOn(cate -> cate.categoryName().equals("CATEGORY_B"))
                .extracting(CategoryBrandPriceResponse::brandName)
                .containsExactly(brandA.getName());

        assertThat(result.totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 데이터가 존재하지않을 경우")
    @Test
    void getCategoryLowestPrices_WhenDataNotExist_ShouldReturnEmptyData() {
        // given
        BigDecimal expectedTotalPrice = new BigDecimal(0L);

        // when
        CategoryLowestPriceResponse result = service.getCategoryLowestPrices();

        // then
        assertThat(result.categoryPrices()).isEmpty();
        assertThat(result.totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회: 같은 최저가 브랜드 존재 시, 이름오름차 순으로 조회")
    @Test
    void getCategoryLowestPrices_WhenDupLowestPriceBrandExist_ShouldReturnDataByBrandNameASC() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal expectedTotalPrice = lowestPrice;

        Brand brandA = brandRepository.save(new Brand("BRAND_A"));
        Brand brandB = brandRepository.save(new Brand("BRAND_B"));
        Category categoryA = categoryRepository.save(new Category("CATEGORY_A"));

        productRepository.save(
                new Product(brandA.getId(), categoryA.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)));
        // when
        CategoryLowestPriceResponse result = service.getCategoryLowestPrices();

        // then
        assertThat(result.categoryPrices())
                .filteredOn(cate -> cate.categoryName().equals("CATEGORY_A"))
                .extracting(CategoryBrandPriceResponse::brandName)
                .containsExactly(brandA.getName());
        assertThat(result.totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 데이터가 존재할 경우")
    @Test
    void getLowestTotalBrandPrice_WhenDataExist_ShouldReturnData() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal highestPrice = new BigDecimal(1000L);
        BigDecimal expectedTotalPrice = lowestPrice.add(lowestPrice);

        Brand brandA = brandRepository.save(new Brand("BRAND_A"));
        Brand brandB = brandRepository.save(new Brand("BRAND_B"));
        Category categoryA = categoryRepository.save(new Category("CATEGORY_A"));
        Category categoryB = categoryRepository.save(new Category("CATEGORY_B"));

        productRepository.save(
                new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)));
        productRepository.save(
                new Product(brandA.getId(), categoryB.getId(), new Money(highestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryB.getId(), new Money(lowestPrice)));
        // when
        BrandLowestPriceResponse result = service.getLowestTotalBrandPrice();

        // then
        assertThat(result.lowestPrice().brandName()).isEqualTo(brandB.getName());
        assertThat(result.lowestPrice().categories())
                .extracting(CategoryPriceResponse::categoryName)
                .containsExactly(categoryA.getName(), categoryB.getName());
        assertThat(result.lowestPrice().totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getLowestTotalBrandPrice_WhenDataNotExist_ShouldReturnEmpty() {
        // given
        BigDecimal expectedTotalPrice = new BigDecimal(0L);

        // when
        BrandLowestPriceResponse result = service.getLowestTotalBrandPrice();

        // then
        assertThat(result.lowestPrice().brandName()).isNull();
        assertThat(result.lowestPrice().categories()).isEmpty();
        assertThat(result.lowestPrice().totalPrice()).isEqualByComparingTo(expectedTotalPrice);
    }

    @DisplayName("단일 브랜드로 모든 카테고리 합이 최저가 조회: 같은 최저가 브랜드 존재 시, 이름오름차 순으로 조회")
    @Test
    void getLowestTotalBrandPrice_WhenDupLowestPriceBrandExist_ShouldReturnData() {
        // given
        BigDecimal lowestPrice = new BigDecimal(100L);
        BigDecimal expectedTotalPrice = lowestPrice.add(lowestPrice);

        Brand brandA = brandRepository.save(new Brand("BRAND_A"));
        Brand brandB = brandRepository.save(new Brand("BRAND_B"));
        Category categoryA = categoryRepository.save(new Category("CATEGORY_A"));
        Category categoryB = categoryRepository.save(new Category("CATEGORY_B"));

        productRepository.save(
                new Product(brandA.getId(), categoryA.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandA.getId(), categoryB.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryB.getId(), new Money(lowestPrice)));
        // when
        BrandLowestPriceResponse result = service.getLowestTotalBrandPrice();

        // then
        assertThat(result.lowestPrice().brandName()).isEqualTo(brandA.getName());
        assertThat(result.lowestPrice().categories())
                .extracting(CategoryPriceResponse::categoryName)
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

        Brand brandA = brandRepository.save(new Brand("BRAND_A"));
        Brand brandB = brandRepository.save(new Brand("BRAND_B"));
        Category categoryA = categoryRepository.save(new Category(categoryName));

        productRepository.save(
                new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryA.getId(), new Money(lowestPrice)));
        // when
        CategoryLowestAndHighestPriceResponse result =
                service.getLowestAndHighestPricesByCategoryName(categoryName);

        // then
        assertThat(result.categoryName()).isEqualTo(categoryName);
        assertThat(result.highestBrandPrices())
                .filteredOn(cate -> cate.price().compareTo(highestPrice) == 0)
                .extracting(BrandPriceResponse::brandName)
                .containsExactly(brandA.getName());
        assertThat(result.lowestBrandPrices())
                .filteredOn(cate -> cate.price().compareTo(lowestPrice) == 0)
                .extracting(BrandPriceResponse::brandName)
                .containsExactly(brandB.getName());
    }

    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회: 데이터가 존재하지 않을 경우")
    @Test
    void getLowestAndHighestPricesByCategoryName_WhenDataNotExist_ShouldReturnData() {
        // given
        String categoryName = "CATEGORY_A";

        // when
        CategoryLowestAndHighestPriceResponse result =
                service.getLowestAndHighestPricesByCategoryName(categoryName);

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

        Brand brandA = brandRepository.save(new Brand("BRAND_A"));
        Brand brandB = brandRepository.save(new Brand("BRAND_B"));
        Brand brandC = brandRepository.save(new Brand("BRAND_C"));
        Brand brandD = brandRepository.save(new Brand("BRAND_D"));
        Category categoryA = categoryRepository.save(new Category(categoryName));

        productRepository.save(
                new Product(brandA.getId(), categoryA.getId(), new Money(highestPrice)));
        productRepository.save(
                new Product(brandB.getId(), categoryA.getId(), new Money(highestPrice)));
        productRepository.save(
                new Product(brandC.getId(), categoryA.getId(), new Money(lowestPrice)));
        productRepository.save(
                new Product(brandD.getId(), categoryA.getId(), new Money(lowestPrice)));

        // when
        CategoryLowestAndHighestPriceResponse result =
                service.getLowestAndHighestPricesByCategoryName(categoryName);

        // then
        assertThat(result.categoryName()).isEqualTo(categoryName);
        assertThat(result.lowestBrandPrices().size()).isEqualTo(2);
        assertThat(result.highestBrandPrices().size()).isEqualTo(2);
    }
}
