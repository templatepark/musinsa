package com.musinsa.product.presentation;

import java.net.URI;

import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.musinsa.product.application.ProductSaveRequest;
import com.musinsa.product.application.ProductService;
import com.musinsa.product.application.ProductUpdateRequest;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(path = "/api/v1/products")
    public ResponseEntity<Void> saveProduct(@RequestBody @Validated ProductSaveRequest request) {
        Long savedProductId = productService.saveProduct(request);
        return ResponseEntity.created(
                        URI.create(String.format("/api/v1/products/%d", savedProductId)))
                .build();
    }

    @DeleteMapping(path = "/api/v1/products/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable(name = "productId") @Min(1L) Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/api/v1/products/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable(name = "productId") @Min(1L) Long productId,
            @RequestBody @Validated ProductUpdateRequest request) {
        productService.updateProduct(productId, request);
        return ResponseEntity.ok().build();
    }
}
