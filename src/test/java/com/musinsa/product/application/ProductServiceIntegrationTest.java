package com.musinsa.product.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.brand.domain.Brand;
import com.musinsa.brand.domain.BrandRepository;
import com.musinsa.category.domain.Category;
import com.musinsa.category.domain.CategoryRepository;
import com.musinsa.product.application.dto.ProductSaveRequest;
import com.musinsa.product.application.dto.ProductUpdateRequest;
import com.musinsa.product.application.exception.NotExistBrandException;
import com.musinsa.product.application.exception.NotExistCategoryException;
import com.musinsa.product.application.exception.NotExistProductException;
import com.musinsa.product.domain.Money;
import com.musinsa.product.domain.Product;
import com.musinsa.product.domain.ProductRepository;

@Transactional
@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired private ProductService service;

    @Autowired private ProductRepository repository;
    @Autowired private BrandRepository brandRepository;
    @Autowired private CategoryRepository categoryRepository;

    private static final BigDecimal PRICE = new BigDecimal(1L);

    @DisplayName("product 저장 성공: brand, category가 존재할 경우")
    @Test
    void saveProduct_WhenExistBrandAndCategory_ShouldReturnProductId() {
        // given
        Long existBrandId = brandRepository.save(new Brand("musinsa")).getId();
        Long existCategoryId = categoryRepository.save(new Category("상의")).getId();
        ProductSaveRequest request = new ProductSaveRequest(existBrandId, existCategoryId, PRICE);

        // when
        Long savedId = service.saveProduct(request);

        // then
        assertThat(savedId).isNotNull();
        assertThat(repository.findById(savedId)).isPresent();
    }

    @DisplayName("product 저장 실패: brand가 존재하지 않을 경우")
    @Test
    void saveProduct_WhenNotExistBrand_ShouldThrowException() {
        // given
        Long notExistId = 1L;
        Long existCategoryId = categoryRepository.save(new Category("상의")).getId();
        ProductSaveRequest request = new ProductSaveRequest(notExistId, existCategoryId, PRICE);

        // when, then
        assertThatThrownBy(() -> service.saveProduct(request))
                .isInstanceOf(NotExistBrandException.class);
    }

    @DisplayName("product 저장 실패: category가 존재하지 않을 경우")
    @Test
    void saveProduct_WhenNotExistCategory_ShouldThrowException() {
        // given
        Long notExistId = 1L;
        Long existBrandId = brandRepository.save(new Brand("musinsa")).getId();
        ProductSaveRequest request = new ProductSaveRequest(existBrandId, notExistId, PRICE);

        // when, then
        assertThatThrownBy(() -> service.saveProduct(request))
                .isInstanceOf(NotExistCategoryException.class);
    }

    @DisplayName("product 삭제 성공: product가 존재할 경우")
    @Test
    void deleteProduct_WhenExistProduct_ShouldSuccess() {
        // given
        Product savedProduct = repository.save(new Product(1L, 1L, new Money(PRICE)));

        // when
        service.deleteProduct(savedProduct.getId());

        // then
        Optional<Product> findProduct = repository.findById(savedProduct.getId());
        assertThat(findProduct).isPresent();
        assertThat(findProduct.get().getDeleted()).isTrue();
    }

    @DisplayName("product 삭제 실패: product가 존재하지 않을 경우")
    @Test
    void deleteProduct_WhenNotExistProduct_ShouldThrowException() {
        // given
        Long notExistId = 1L;

        // when, then
        assertThatThrownBy(() -> service.deleteProduct(notExistId))
                .isInstanceOf(NotExistProductException.class);
    }

    @DisplayName("product 업데이트 성공: product, brand, category가 존재할 경우")
    @Test
    void updateProduct_WhenExistProduct_ShouldSuccess() {
        // given
        BigDecimal changedPrice = new BigDecimal(2L);
        Brand originBrand = brandRepository.save(new Brand("BRAND_A"));
        Brand changedBrand = brandRepository.save(new Brand("BRAND_B"));
        Category originCategory = categoryRepository.save(new Category("CATEGORY_A"));
        Category changedCategory = categoryRepository.save(new Category("CATEGORY_B"));

        Product savedProduct =
                repository.save(
                        new Product(originBrand.getId(), originCategory.getId(), new Money(PRICE)));
        ProductUpdateRequest request =
                new ProductUpdateRequest(
                        changedBrand.getId(), changedCategory.getId(), changedPrice);

        // when
        service.updateProduct(savedProduct.getId(), request);

        // then
        Optional<Product> findProduct = repository.findById(savedProduct.getId());
        assertThat(findProduct).isPresent();
        assertThat(findProduct.get().getBrandId()).isEqualTo(changedBrand.getId());
        assertThat(findProduct.get().getCategoryId()).isEqualTo(changedCategory.getId());
        assertThat(findProduct.get().getPrice().getValue()).isEqualTo(changedPrice);
    }

    @DisplayName("product 업데이트 실패: product가 존재하지 않을 경우")
    @Test
    void updateProduct_WhenNotExistProduct_ShouldThrowException() {
        // given
        Long notExistId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest(2L, 2L, new BigDecimal(2L));

        // when, then
        assertThatThrownBy(() -> service.updateProduct(notExistId, request))
                .isInstanceOf(NotExistProductException.class);
    }

    @DisplayName("product 업데이트 실패: brand가 존재하지 않을 경우")
    @Test
    void updateProduct_WhenNotExistBrand_ShouldThrowException() {
        // given
        Long notExistId = 2L;
        Product savedProduct = repository.save(new Product(1L, 1L, new Money(PRICE)));
        ProductUpdateRequest request = new ProductUpdateRequest(notExistId, 1L, new BigDecimal(2L));

        // when, then
        assertThatThrownBy(() -> service.updateProduct(savedProduct.getId(), request))
                .isInstanceOf(NotExistBrandException.class);
    }

    @DisplayName("product 업데이트 실패: category가 존재하지 않을 경우")
    @Test
    void updateProduct_WhenNotExistCategory_ShouldThrowException() {
        // given
        Long notExistId = 2L;
        Product savedProduct = repository.save(new Product(1L, 1L, new Money(PRICE)));
        Brand brandA = brandRepository.save(new Brand("BRAND_A"));
        ProductUpdateRequest request =
                new ProductUpdateRequest(brandA.getId(), notExistId, new BigDecimal(2L));

        // when, then
        assertThatThrownBy(() -> service.updateProduct(savedProduct.getId(), request))
                .isInstanceOf(NotExistCategoryException.class);
    }
}
