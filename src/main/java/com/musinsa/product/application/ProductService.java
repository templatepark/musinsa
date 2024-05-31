package com.musinsa.product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.brand.domain.BrandRepository;
import com.musinsa.category.domain.CategoryRepository;
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

    @Transactional
    public void deleteProduct(Long productId) {
        Product findProduct = findProductByIdOrThrow(productId);
        findProduct.delete();
    }

    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest request) {
        Product findProduct = findProductByIdOrThrow(productId);
        findProduct.updateFrom(request.toProduct());
    }

    private Product findProductByIdOrThrow(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NotExistProductException("해당 프로덕트가 존재하지 않습니다."));
    }
}
