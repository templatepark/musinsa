package com.musinsa.product.presentation.api;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musinsa.product.application.ProductQueryService;
import com.musinsa.product.application.dto.BrandLowestPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestAndHighestPriceResponse;
import com.musinsa.product.application.dto.CategoryLowestPriceResponse;

@RestController
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    public ProductQueryController(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    @GetMapping(path = "/api/v1/products/category-lowest-prices")
    public ResponseEntity<CategoryLowestPriceResponse> getCategoryLowestPrices() {
        return ResponseEntity.ok().body(productQueryService.getCategoryLowestPrices());
    }

    @GetMapping(path = "/api/v1/products/lowest-total-brand-price")
    public ResponseEntity<BrandLowestPriceResponse> getLowestTotalBrandPrice() {
        return ResponseEntity.ok().body(productQueryService.getLowestTotalBrandPrice());
    }

    @GetMapping(path = "/api/v1/products/category-lowest-highest-prices")
    public ResponseEntity<CategoryLowestAndHighestPriceResponse>
            getLowestAndHighestPricesByCategoryName(
                    @RequestParam(name = "categoryName") @NotBlank String categoryName) {
        return ResponseEntity.ok()
                .body(productQueryService.getLowestAndHighestPricesByCategoryName(categoryName));
    }
}
