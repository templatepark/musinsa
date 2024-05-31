package com.musinsa.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.musinsa.brand.domain.Brand;
import com.musinsa.brand.domain.BrandRepository;
import com.musinsa.category.domain.Category;
import com.musinsa.category.domain.CategoryRepository;
import com.musinsa.product.domain.Money;
import com.musinsa.product.domain.Product;
import com.musinsa.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock private ProductRepository productRepository;
    @Mock private BrandRepository brandRepository;
    @Mock private CategoryRepository categoryRepository;

    @InjectMocks private ProductService service;

    private static final Long BRAND_ID = 1L;
    private static final Long CATEGORY_ID = 1L;
    private static final BigDecimal PRICE = new BigDecimal(1L);
    private static final ProductSaveRequest SAVE_REQUEST =
            new ProductSaveRequest(BRAND_ID, CATEGORY_ID, PRICE);

    @DisplayName("product 저장 성공: brand, category가 존재할 경우")
    @Test
    void saveProduct_WhenExistBrandAndCategory_ShouldReturnProductId() {
        // given
        Long expectProductId = 1L;

        Brand returnBrand = new Brand("name");
        Category returnCategory = new Category("name");
        Product returnProduct = new Product(BRAND_ID, CATEGORY_ID, new Money(PRICE));
        ReflectionTestUtils.setField(returnProduct, "id", expectProductId);

        given(brandRepository.findById(anyLong())).willReturn(Optional.of(returnBrand));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(returnCategory));
        given(productRepository.save(any())).willReturn(returnProduct);

        // when
        Long resultId = service.saveProduct(SAVE_REQUEST);

        // then
        assertThat(resultId).isEqualTo(expectProductId);
        then(brandRepository).should().findById(anyLong());
        then(categoryRepository).should().findById(anyLong());
        then(productRepository).should().save(any());
    }

    @DisplayName("product 저장 실패: brand가 존재하지 않을 경우")
    @Test
    void saveProduct_WhenNotExistBrand_ShouldThrowException() {
        // given
        given(brandRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> service.saveProduct(SAVE_REQUEST))
                .isInstanceOf(NotExistBrandException.class);
        then(brandRepository).should().findById(anyLong());
    }

    @DisplayName("product 저장 실패: category가 존재하지 않을 경우")
    @Test
    void saveProduct_WhenNotExistCategory_ShouldThrowException() {
        // given
        given(brandRepository.findById(anyLong())).willReturn(Optional.of(new Brand("name")));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> service.saveProduct(SAVE_REQUEST))
                .isInstanceOf(NotExistCategoryException.class);
        then(brandRepository).should().findById(anyLong());
        then(categoryRepository).should().findById(anyLong());
    }
}
