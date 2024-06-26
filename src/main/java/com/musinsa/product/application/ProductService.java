package com.musinsa.product.application;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.brand.domain.BrandRepository;
import com.musinsa.category.domain.CategoryRepository;
import com.musinsa.common.constants.CacheNames;
import com.musinsa.product.application.dto.ProductSaveRequest;
import com.musinsa.product.application.dto.ProductUpdateRequest;
import com.musinsa.product.application.exception.NotExistBrandException;
import com.musinsa.product.application.exception.NotExistCategoryException;
import com.musinsa.product.application.exception.NotExistProductException;
import com.musinsa.product.domain.Product;
import com.musinsa.product.domain.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @CacheEvict(
            value = {
                CacheNames.CATEGORY_LOWEST_PRICES,
                CacheNames.BRAND_TOTAL_LOWEST_PRICE,
                CacheNames.CATEGORY_LOWEST_HIGHEST_PRICES
            },
            allEntries = true)
    @Transactional
    public Long saveProduct(ProductSaveRequest request) {
        validateExistBrand(request.brandId());
        validateExistCategory(request.categoryId());
        return productRepository.save(request.toProduct()).getId();
    }

    private void validateExistBrand(Long brandId) {
        brandRepository
                .findById(brandId)
                .orElseThrow(() -> new NotExistBrandException("해당 브랜드가 존재하지 않습니다."));
    }

    private void validateExistCategory(Long categoryId) {
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotExistCategoryException("해당 카테고리가 존재하지 않습니다."));
    }

    @CacheEvict(
            value = {
                CacheNames.CATEGORY_LOWEST_PRICES,
                CacheNames.BRAND_TOTAL_LOWEST_PRICE,
                CacheNames.CATEGORY_LOWEST_HIGHEST_PRICES
            },
            allEntries = true)
    @Transactional
    public void deleteProduct(Long productId) {
        Product findProduct = findProductByIdOrThrow(productId);
        findProduct.delete();
    }

    @CacheEvict(
            value = {
                CacheNames.CATEGORY_LOWEST_PRICES,
                CacheNames.BRAND_TOTAL_LOWEST_PRICE,
                CacheNames.CATEGORY_LOWEST_HIGHEST_PRICES
            },
            allEntries = true)
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest request) {
        Product findProduct = findProductByIdOrThrow(productId);
        validateExistBrand(request.brandId());
        validateExistCategory(request.categoryId());
        findProduct.updateFrom(request.toProduct());
    }

    private Product findProductByIdOrThrow(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NotExistProductException("해당 프로덕트가 존재하지 않습니다."));
    }
}
