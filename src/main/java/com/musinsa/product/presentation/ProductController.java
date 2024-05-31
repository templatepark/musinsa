package com.musinsa.product.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.musinsa.product.application.ProductSaveRequest;
import com.musinsa.product.application.ProductService;

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
}
